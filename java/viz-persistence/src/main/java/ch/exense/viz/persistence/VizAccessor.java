package ch.exense.viz.persistence;

import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.exense.viz.persistence.accessors.MongoClientSession;

public class VizAccessor {

	private static final Logger logger = LoggerFactory.getLogger(VizAccessor.class);

	private MongoClientSession client;
	
	public VizAccessor(MongoClientSession client) {
		this.client = client;
	}
	
	public static enum DashboardCollection{
		SESSIONS("sessions"), DASHBOARDS("dashboards"), DASHLETS("dashlets"), SERVICES("services"),
		QUERIES("queries"), PROCESSORS("processors"), FUNCTIONS("functions");
		
		private String collname;
		DashboardCollection(String collname) {
	        this.setCollname(collname);
	    }
		public String getCollname() {
			return collname;
		}
		public void setCollname(String collname) {
			this.collname = collname;
		}
	}

	public void insertObject(Object obj, DashboardCollection collection){
		client.getJongoCollection(collection.getCollname()).insert(obj);
		logger.debug("Inserted " + obj.toString() + " into " + collection.getCollname());
	}
	
	public <T> T findByAttribute(String attributeName, Object attributeValue, DashboardCollection collection, Class<T> asType){
		return client.getJongoCollection(collection.getCollname()).findOne(new Document().append(attributeName, attributeValue).toString()).as(asType);
	}

}
