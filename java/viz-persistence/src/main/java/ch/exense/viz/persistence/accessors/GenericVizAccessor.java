package ch.exense.viz.persistence.accessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.exense.viz.persistence.mongodb.MongoClientSession;

public class GenericVizAccessor {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GenericVizAccessor.class);

	private MongoClientSession session;
	
	public GenericVizAccessor(MongoClientSession session) {
		this.session = session;
	}

	public void insertObject(Object obj, String collection){
		session.getJongoCollection(collection).insert(obj);
	}
	
	public <T> T findByAttribute(String attributeName, Object attributeValue, String collection, Class<T> asType){
		return session.getJongoCollection(collection).findOne(new Document().append(attributeName, attributeValue).toJson()).as(asType);
	}

	public void removeByAttribute(String attributeName, Object attributeValue, String collection){
		session.getJongoCollection(collection).remove(new Document().append(attributeName, attributeValue).toJson());
	}
	
	// Unstreamed db result for basic queries
	public List<Object> execute(String collection, String query, int skip, int limit, String sort, String projection){
		MongoCursor<Object> cursor = session.getJongoCollection(collection).find(query).skip(skip).limit(limit).sort(sort).projection(projection).as(Object.class);
		List<Object> result = new ArrayList<Object>();
		cursor.forEach(e -> result.add(e));
		try {
			cursor.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}
}
