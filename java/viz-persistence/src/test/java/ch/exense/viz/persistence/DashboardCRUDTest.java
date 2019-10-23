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
import ch.exense.viz.persistence.accessors.AbstractCRUDAccessor;
import ch.exense.viz.persistence.accessors.MongoClientSession;
import ch.exense.viz.persistence.model.Dashboard;

public class DashboardCRUDTest {

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
	public void genericAccessorTest() {
		Dashboard d = new Dashboard();
		d.setTitle("foo");
		d.setOid("bar");
		AbstractCRUDAccessor<Dashboard> accessor = new AbstractCRUDAccessor<>(session, "dashboards", Dashboard.class);
		Dashboard saved = accessor.save(d);
		
		Map<String, String> attributes = new HashMap<>();
		attributes.put("title", "foo");
		Assert.assertEquals("foo", accessor.findByAttributes(attributes, null).getTitle());
		accessor.remove(saved.getId());
		Assert.assertNull(accessor.findByAttributes(attributes, null));
	}
}
