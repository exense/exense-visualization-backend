package ch.exense.viz.persistence.accessors;

import org.bson.Document;
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
}
