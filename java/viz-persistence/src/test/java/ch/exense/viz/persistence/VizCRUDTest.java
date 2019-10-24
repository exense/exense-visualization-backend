package ch.exense.viz.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jongo.MongoCollection;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.exense.commons.app.Configuration;
import ch.exense.viz.persistence.accessors.GenericVizAccessor;
import ch.exense.viz.persistence.accessors.typed.DashboardAccessor;
import ch.exense.viz.persistence.model.Dashboard;

public class VizCRUDTest {

	private static Configuration config;
	private static MongoClientSession session;
	private static MongoCollection dashboards;
	
	@BeforeClass
	public static void beforeClass() {
		config = new Configuration();
		config.putProperty("isTestDb", "true");
		
		session = new MongoClientSession(config);
		dashboards = session.getJongoCollection("dashboards");
		dashboards.drop();
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
		session.close();
	}
	
	@Test
	public void basicCRUDTest() throws IOException {
		Dashboard d = new Dashboard();
		d.setTitle("foo");
		d.setOid("bar");
		dashboards.insert(d);
		Assert.assertEquals("foo", dashboards.findOne("{}").as(Dashboard.class).getTitle());
		dashboards.remove("{}");
		Assert.assertNull(dashboards.findOne("{}").as(Dashboard.class));
	}
	
	@Test
	public void genericVizAccessorTest() {
		Dashboard d = new Dashboard();
		d.setTitle("foo");
		d.setOid("bar");
		GenericVizAccessor accessor = new GenericVizAccessor(session);
		accessor.insertObject(d, "dashboards");
		
		Assert.assertEquals("foo", accessor.findByAttribute("title", "foo", "dashboards", Dashboard.class).getTitle());
		accessor.removeByAttribute("title", "foo", "dashboards");
		Assert.assertNull(accessor.findByAttribute("title", "foo", "dashboards", Dashboard.class));
	}
	
	@Test
	public void typedAccessorTest() {
		Dashboard d = new Dashboard();
		d.setTitle("foo");
		d.setOid("bar");
		DashboardAccessor accessor = new DashboardAccessor(session);
		Dashboard saved = accessor.save(d);
		
		Map<String, String> attributes = new HashMap<>();
		attributes.put("title", "foo");
		Assert.assertEquals("foo", accessor.findByAttributes(attributes, null).getTitle());
		accessor.remove(saved.getId());
		Assert.assertNull(accessor.findByAttributes(attributes, null));
	}

}
