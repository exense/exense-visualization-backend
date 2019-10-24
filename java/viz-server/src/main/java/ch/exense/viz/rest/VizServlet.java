package ch.exense.viz.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
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

@Path("/crud")
public class VizServlet{

	private static final Logger logger = LoggerFactory.getLogger(VizServlet.class);

	@Inject
	GenericVizAccessor accessor;
			
	@POST
	@Path("/{collection}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSession(@PathParam(value = "collection") String collection, final Object vizObject) {
		logger.info("Saving object: " + vizObject + " to collection: " + collection);
		this.accessor.insertObject(vizObject, collection);
		return Response.status(200).entity("{ \"status\" : \"ok\"}").build();
	}
	
	@GET
	@Path("/{collection}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object loadSession(@PathParam(value = "collection") String collection, @QueryParam(value = "name") String name) {
		logger.info("Loading object by name: " + name + " from collection: " + collection);
		return Response.status(200).entity(this.accessor.findByAttribute("name", name, collection, Object.class)).build();
	}

}