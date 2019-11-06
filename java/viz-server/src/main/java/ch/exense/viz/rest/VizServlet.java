package ch.exense.viz.rest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.exense.viz.persistence.accessors.GenericVizAccessor;
import ch.exense.viz.proxy.ProxiedRequest;
import ch.exense.viz.proxy.ProxiedResponse;
import ch.exense.viz.proxy.ProxyService;

@Singleton
@Path("/viz")
public class VizServlet{

	private static final Logger logger = LoggerFactory.getLogger(VizServlet.class);

	@Inject
	GenericVizAccessor accessor;

	@POST
	@Path("/crud/{collection}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveObject(@PathParam(value = "collection") String collection, @QueryParam(value = "name") String name, final Object vizObject) {
		logger.info("Saving object: " + vizObject + " to collection: " + collection);
		Object found = this.accessor.findByAttribute("name", name, collection, Object.class);
		if(found != null) {
			this.accessor.removeByAttribute("name", name, collection);
			logger.debug("Removed existing object: " + found + " with name: " + name);
		}
		this.accessor.insertObject(vizObject, collection);
		return Response.status(200).entity(found).build();
	}

	@GET
	@Path("/crud/{collection}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object loadObject(@PathParam(value = "collection") String collection, @QueryParam(value = "name") String name) {
		logger.debug("Loading object by name: " + name + " from collection: " + collection);
		return Response.status(200).entity(this.accessor.findByAttribute("name", name, collection, Object.class)).build();
	}

	@DELETE
	@Path("/crud/{collection}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object deleteObject(@PathParam(value = "collection") String collection, @QueryParam(value = "name") String name) {
		logger.debug("Removing object by name: " + name + " from collection: " + collection);
		this.accessor.removeByAttribute("name", name, collection);
		return Response.status(200).entity(null).build();
	}

	@POST
	@Path("/proxy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response proxyQuery(ProxiedRequest request) {
		ProxiedResponse response = new ProxyService().executeProxiedQuery(request);
		return Response.status(response.getCode()).entity(response.getData()).build();
	}

	@POST
	@Path("/driver")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response driverQuery(DirectMongoQuery request) {
		List<Object> result = null;
		try {
			if(request.getCollection() == null || request.getCollection().trim().isEmpty()) {
				throw new Exception("Please provide a collection name.");
			}
			
			if(request.getHost() != null
					//&& !request.getHost().trim().toLowerCase().equals("localhost")
					//&& !request.getHost().trim().toLowerCase().equals("127.0.0.1")
					) {
					result = accessor.execute(request.getHost(), request.getPort(), request.getDatabase(), request.getCollection(), request.getQuery(), request.getSkip(), request.getLimit(), request.getSort(), request.getProjection());
				}else {
				result = accessor.execute(request.getCollection(), request.getQuery(), request.getSkip(), request.getLimit(), request.getSort(), request.getProjection());
			}
			return Response.status(200)
					.entity(result)
					.build();
		}catch(Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("{ \"error\" : \""+e.getClass() + ": "+ e.getMessage()+"\"}")
					.build();
		}
	}
}