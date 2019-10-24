package ch.exense.viz.persistence.accessors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.exense.viz.persistence.MongoClientSession;

public class GenericVizAccessor {

	private static final Logger logger = LoggerFactory.getLogger(GenericVizAccessor.class);

	private MongoClientSession session;
	
	public GenericVizAccessor(MongoClientSession session) {
		this.session = session;
	}
	
	public static enum VizCollection{
		SESSIONS("sessions"), DASHBOARDS("dashboards"), DASHLETS("dashlets"), SERVICES("services"),
		QUERIES("queries"), PROCESSORS("processors"), FUNCTIONS("functions");
		
		private String collname;
		VizCollection(String collname) {
	        this.setCollname(collname);
	    }
		public String getCollname() {
			return collname;
		}
		public void setCollname(String collname) {
			this.collname = collname;
		}
	}

	public void insertObject(Object obj, VizCollection collection){
		session.getJongoCollection(collection.getCollname()).insert(obj);
		logger.debug("Inserted " + obj.toString() + " into " + collection.getCollname());
	}
	
	public <T> T findByAttribute(String attributeName, Object attributeValue, VizCollection collection, Class<T> asType){
		return session.getJongoCollection(collection.getCollname()).findOne(new Document().append(attributeName, attributeValue).toJson()).as(asType);
	}

	public void removeByAttribute(String attributeName, Object attributeValue, VizCollection collection){
		session.getJongoCollection(collection.getCollname()).remove(new Document().append(attributeName, attributeValue).toJson());
	}
}
