package ch.exense.viz.persistence.accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.exense.commons.app.Configuration;
import step.core.collections.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import step.core.collections.mongodb.MongoDBCollectionFactory;

public class GenericVizAccessor {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GenericVizAccessor.class);

	private CollectionFactory collectionFactory;
	
	public GenericVizAccessor(CollectionFactory collectionFactory) {
		this.collectionFactory = collectionFactory;
	}

	public void insertObject(ObjectWrapper obj, String collection){
		collectionFactory.getCollection(collection, ObjectWrapper.class).save(obj);
	}
	
	public <T> T findByAttribute(String attributeName, String attributeValue, String collection, Class<T> asType){
		Collection<T> driver = collectionFactory.getCollection(collection, asType);
		return driver.find(Filters.equals(attributeName,attributeValue),null,null,null,0)
				.findFirst().orElse(null);
	}

	public void removeByAttribute(String attributeName, String attributeValue, String collection){
		collectionFactory.getCollection(collection, ObjectWrapper.class).remove(Filters.equals(attributeName,attributeValue));
	}
	
	public long count(String collection){
		Collection<ObjectWrapper> driver = collectionFactory.getCollection(collection, ObjectWrapper.class);
		return driver.find(Filters.empty(), null, null, null, 0).count();
	}

	public long count(String collection, Filter filter){
		Collection<ObjectWrapper> driver = collectionFactory.getCollection(collection, ObjectWrapper.class);
		return driver.find(filter, null, null, null, 0).count();
	}
	
	public MongoResult getAll(String collection){
		return execute(collection, Filters.empty(), 0, 0, null, "");
	}
	
	public MongoResult getAll(String collection, int skip, int limit, SearchOrder order){
		return execute(collection, Filters.empty(), skip, limit, order, "");
	}
	
	// Unstreamed db result for basic queries
	public MongoResult execute(String collection, Filter filter, int skip, int limit, SearchOrder order, String projection){
		Collection<ObjectWrapper> driver = collectionFactory.getCollection(collection, ObjectWrapper.class);
		List<String> projectionField = (projection!=null && !projection.isEmpty()) ? 
				Arrays.asList(projection.split(",")) : null;
		Stream<ObjectWrapper> objectStream = driver.findReduced(filter, order, skip, limit, 0, projectionField);
		return consumeStream(objectStream);
	}
	
//	TODO 
	public List<ObjectWrapper> execute(String host, int port, String database, String collection, Filter filter, int skip, int limit, SearchOrder order, String projection){
		Configuration configuration = new Configuration();
		configuration.putProperty("db.host", host);
		configuration.putProperty("db.port", String.valueOf(port));
		configuration.putProperty("db.database", database);
		MongoDBCollectionFactory remoteCollectionFactory = new MongoDBCollectionFactory(configuration.getUnderlyingPropertyObject());
		Collection<Document> driver = remoteCollectionFactory.getCollection(collection, Document.class);

		List<String> projectionField = (projection!=null && !projection.isEmpty()) ?
				Arrays.asList(projection.split(",")) : null;
		Stream<Document> objectStream = driver.findReduced(filter, order, skip, limit, 0, projectionField);
		return consumeStream(objectStream.collect(Collectors.toList()));
	}

	private List<ObjectWrapper> consumeStream(Iterable<Document> stream) {
		List<ObjectWrapper> result = new ArrayList<>();
		stream.forEach(d -> result.add(new ObjectWrapper((String)d.get("name"), d)));
		return result;
	}
	
	private MongoResult consumeStream(Stream<ObjectWrapper> stream) {
		MongoResult result = new MongoResult();
		List<ObjectWrapper> data = stream.collect(Collectors.toList());
		result.setData(data);
		result.setCount(data.size());
		return result;
	}
	
}
