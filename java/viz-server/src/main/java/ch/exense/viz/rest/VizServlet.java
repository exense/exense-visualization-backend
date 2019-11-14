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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.exense.viz.persistence.accessors.DataTableWrapper;
import ch.exense.viz.persistence.accessors.GenericVizAccessor;
import ch.exense.viz.persistence.accessors.MongoResult;
import ch.exense.viz.persistence.accessors.ObjectWrapper;
import ch.exense.viz.persistence.accessors.PagedDataTableWrapper;
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
		this.accessor.insertObject(new ObjectWrapper(name, vizObject), collection);
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
	
	@GET
	@Path("/crud/all/{collection}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object getAll(@PathParam(value = "collection") String collection) {
		logger.debug("Loading full collection: " + collection);
		return Response.status(200).entity(new DataTableWrapper(this.accessor.getAll(collection).getData())).build();
	}

	@POST
	@Path("/crud/paged/{collection}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getData(@PathParam(value = "collection") String collection, MultivaluedMap<String, String> form) {
		logger.debug("Loading paged collection: " + collection);
		int draw = Integer.parseInt(form.get("draw").get(0));
		int skip = Integer.parseInt(form.get("start").get(0));
		int limit = skip + Integer.parseInt(form.get("length").get(0));
		String searchValue = form.get("search[value]").get(0);
		String query = "{}";
		
		if(searchValue != null && !searchValue.isEmpty()) {
			query = "{\"name\": { $regex : '"+searchValue+"' }}";
		}
		
		MongoResult result = this.accessor.execute(collection, query, skip, limit, "{\"name\" : 1}", "");
		PagedDataTableWrapper pagedDataTableResponse = toPagedDataTable(result.getData(), draw, result.getCount());
		return Response.status(200).entity(pagedDataTableResponse).build();
	}
	
	private PagedDataTableWrapper toPagedDataTable(List<ObjectWrapper> data, int draw, long count) {
		PagedDataTableWrapper wrapper = new PagedDataTableWrapper(data);
		wrapper.setDraw((Integer)draw);
		//not supporting search yet so recordsFiltered = recordsTotal
		wrapper.setRecordsFiltered(count);
		wrapper.setRecordsTotal(count);
		return wrapper;
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
		List<ObjectWrapper> result = null;
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
				result = accessor.execute(request.getCollection(), request.getQuery(), request.getSkip(), request.getLimit(), request.getSort(), request.getProjection()).getData();
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