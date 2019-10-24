package ch.exense.viz.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import ch.exense.commons.app.Configuration;
import ch.exense.viz.persistence.accessors.GenericVizAccessor;
import ch.exense.viz.persistence.mongodb.MongoClientSession;

public class VizServer {

	private Server server;

	public static void main(String[] args){
		try {
			new VizServer().start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void start() throws Exception {

		this.server = new Server(8199);
		
		Configuration config = new Configuration();
		config.putProperty("isTestDb", "true");
		
		ContextHandlerCollection hcoll = new ContextHandlerCollection();
		
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(VizServlet.class);
		resourceConfig.register(JacksonJaxbJsonProvider.class);
		
		GenericVizAccessor accessor = new GenericVizAccessor(new MongoClientSession(config));
		resourceConfig.register(new AbstractBinder() {	
			@Override
			protected void configure() {
				bind(accessor).to(GenericVizAccessor.class);
			}
		});
		
		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		ServletHolder sh = new ServletHolder(servletContainer);
		sh.setInitParameter("cacheControl","max-age=0,public"); 

		ServletContextHandler serviceHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		serviceHandler.setContextPath("/viz");
		serviceHandler.addServlet(sh, "/*");
		serviceHandler.setInitParameter("cacheControl","max-age=0,public"); 

		hcoll.addHandler(serviceHandler);
		server.setHandler(hcoll);
		server.start();
		server.join();	
	}
	
}
