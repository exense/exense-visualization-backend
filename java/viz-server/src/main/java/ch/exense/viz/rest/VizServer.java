package ch.exense.viz.rest;

import step.core.collections.CollectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import ch.exense.commons.app.Configuration;
import ch.exense.viz.persistence.accessors.GenericVizAccessor;

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
		
		Configuration configuration = new Configuration();
		configuration.putProperty("isTestDb", "true");
		
		ContextHandlerCollection hcoll = new ContextHandlerCollection();
		
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(VizServlet.class);
		resourceConfig.register(JacksonJaxbJsonProvider.class);

		//String collectionClassname = configuration.getProperty("db.type", FilesystemCollectionFactory.class.getName());
		//Since migration to mongojack and newest mongo driver we cannot use jongo. Session contains field with do which
		//can only be inserted in mongo with jongo (mongo driver does validation and throw an exception)
		String collectionClassname = "FilesystemCollectionFactory.class.getName()";
		CollectionFactory collectionFactory = (CollectionFactory) Class.forName(collectionClassname)
				.getConstructor(Configuration.class).newInstance(configuration);
		
		GenericVizAccessor accessor = new GenericVizAccessor(collectionFactory);
		resourceConfig.register(new AbstractBinder() {	
			@Override
			protected void configure() {
				bind(accessor).to(GenericVizAccessor.class);
			}
		});
		
		ResourceHandler bb = new ResourceHandler();
		bb.setResourceBase(Resource.newClassPathResource("webapp").getURI().toString());
		
		ContextHandler ctx = new ContextHandler("/");
		ctx.setHandler(bb);
		
		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		ServletHolder sh = new ServletHolder(servletContainer);
		sh.setInitParameter("cacheControl","max-age=0,public"); 

		ServletContextHandler serviceHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		serviceHandler.setContextPath("/rest");
		serviceHandler.addServlet(sh, "/*");
		serviceHandler.setInitParameter("cacheControl","max-age=0,public"); 

		hcoll.addHandler(serviceHandler);
		hcoll.addHandler(ctx);
		server.setHandler(hcoll);
		server.start();
		server.join();	
	}
	
}
