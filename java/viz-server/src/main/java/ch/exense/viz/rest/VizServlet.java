package ch.exense.viz.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.exense.viz.persistence.accessors.GenericVizAccessor;
import ch.exense.viz.persistence.accessors.GenericVizAccessor.VizCollection;
import ch.exense.viz.persistence.model.Session;

@Path("/crud")
public class VizServlet{

	private static final Logger logger = LoggerFactory.getLogger(VizServlet.class);

	@Inject
	GenericVizAccessor accessor;
			
	@POST
	@Path("/session")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSession(final Session session) {
		logger.debug("Saving session: " + session);
		this.accessor.insertObject(session, VizCollection.SESSIONS);
		return Response.status(200).entity("{ \"status\" : \"ok\"}").build();
	}
	
	@GET
	@Path("/session")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object loadSession(@QueryParam(value = "name") String name) {
		logger.debug("Loading session: " + name);
		return Response.status(200).entity(this.accessor.findByAttribute("name", name, VizCollection.SESSIONS, Session.class)).build();
	}

}