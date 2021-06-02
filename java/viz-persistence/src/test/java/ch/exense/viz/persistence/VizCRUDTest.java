package ch.exense.viz.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.exense.viz.persistence.accessors.ObjectWrapper;
import step.core.collections.Collection;
import step.core.collections.Filters;
import step.core.collections.filesystem.FilesystemCollectionFactory;
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
	private static FilesystemCollectionFactory fsCollectionFactory;
	private static Collection<Dashboard> dashboardsCol;
	private static DashboardAccessor dashboards;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		fsCollectionFactory = new FilesystemCollectionFactory(getConfiguration());

		dashboardsCol = fsCollectionFactory.getCollection(Dashboard.ENTITY_NAME, Dashboard.class);
		dashboards = new DashboardAccessor(dashboardsCol);
		dashboardsCol.drop();
	}
	

	private static Configuration getConfiguration() throws IOException {
		Configuration configuration = new Configuration();
		configuration.putProperty("db.filesystem.path","C:\\Tools\\step-db-test");
		//support again embedded mongo db for tests???, not for now:
		// mongo not supported anymore since migration to new driver and removal of jongo
		return configuration;
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
	}
	
	@Test
	public void basicCRUDTest() throws IOException {
		Dashboard d = new Dashboard();
		d.setTitle("foo");
		d.setOid("bar");
		dashboardsCol.save(d);
		List<Dashboard> result = dashboardsCol.find(Filters.empty(), null, null, null, 0).collect(Collectors.toList());
		Assert.assertEquals("foo", result.get(0).getTitle());
		dashboardsCol.remove(Filters.empty());
		result.clear();
		result = dashboardsCol.find(Filters.empty(), null, null, null, 0).collect(Collectors.toList());
		Assert.assertTrue(result.isEmpty());
	}
	
	@Test
	public void genericVizAccessorTest() {
		Map<String,Object> d = new HashMap<>() ;
		d.put("title","foo");
		d.put("oid","bar");
		GenericVizAccessor accessor = new GenericVizAccessor(fsCollectionFactory);
		accessor.insertObject(new ObjectWrapper("foo",d), "sessions");
		ObjectWrapper byAttribute = accessor.findByAttribute("object.title", "foo", "sessions", ObjectWrapper.class);
		Map<String,Object> object = byAttribute.getObject();
		Assert.assertEquals("foo", object.get("title").toString());
		accessor.removeByAttribute("object.title", "foo", "sessions");
		Assert.assertNull(accessor.findByAttribute("object.title", "foo", "sessions", ObjectWrapper.class));
	}
	
	@Test
	public void typedAccessorTest() {
		Dashboard d = new Dashboard();
		d.setTitle("foo");
		d.setOid("bar");
		
		DashboardAccessor accessor = new DashboardAccessor(dashboardsCol);
		Dashboard saved = accessor.save(d);
		
		Map<String, String> attributes = new HashMap<>();
		attributes.put("title", "foo");
		//Currently only search by field 'attributes' is possible and dashboard has none
		Assert.assertEquals("foo", accessor.get(d.getId()).getTitle());
		accessor.remove(saved.getId());
		Assert.assertNull(accessor.findByAttributes(attributes, null));
	}

}
