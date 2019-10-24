/*******************************************************************************
 * (C) Copyright 2016 Jerome Comte and Dorian Cransac
 *  
 * This code is protected by the  GNU Affero General Public License
 * See <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package ch.exense.viz.persistence.mongodb;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import ch.exense.commons.app.Configuration;
import ch.exense.viz.persistence.accessors.DefaultAccessorModule;
import ch.exense.viz.persistence.accessors.EmbeddedMongo;

public class MongoClientSession implements Closeable {

	protected MongoClient mongoClient;

	protected String db;

	protected Configuration configuration;

	private boolean isTestDb;

	private EmbeddedMongo embededMongo;

	public MongoClientSession(Configuration configuration) {
		super();
		this.configuration = configuration;

		String isTestDb = configuration.getProperty("isTestDb");
		if(isTestDb != null && !isTestDb.trim().toLowerCase().equals("false")) {
			this.isTestDb = true;
		}

		initMongoClient();
	}

	protected void initMongoClient() {
		if(this.isTestDb) {
			this.embededMongo = new EmbeddedMongo("./testdb", "localhost", 27987);
			try {
				this.embededMongo.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.mongoClient = this.embededMongo.getClient();
			db = "step";
		}else {

			String host = configuration.getProperty("db.host");
			Integer port = configuration.getPropertyAsInteger("db.port",27017);
			String user = configuration.getProperty("db.username");
			String pwd = configuration.getProperty("db.password");
			int maxConnections = configuration.getPropertyAsInteger("db.maxConnections", 200);

			db = configuration.getProperty("db.database","step");

			ServerAddress address = new ServerAddress(host, port);
			List<MongoCredential> credentials = new ArrayList<>();
			if(user!=null) {
				MongoCredential credential = MongoCredential.createCredential(user, db, pwd.toCharArray());
				credentials.add(credential);
			}
			MongoClientOptions.Builder clientOptions = new MongoClientOptions.Builder();
			MongoClientOptions options = clientOptions.connectionsPerHost(maxConnections).build();
			mongoClient = new MongoClient(address, credentials,options);
		}
	}

	public MongoDatabase getMongoDatabase() {
		return mongoClient.getDatabase(db);
	}
	

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public org.jongo.MongoCollection getJongoCollection(String collectionName) {
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB(this.db);

		Jongo jongo = new Jongo(db,new JacksonMapper.Builder()
				.registerModule(new JSR353Module())
				.registerModule(new JsonOrgModule())
				.registerModule(new DefaultAccessorModule())
				.build());
		MongoCollection collection = jongo.getCollection(collectionName);

		return collection;
	}

	@Override
	public void close() throws IOException {
		if(this.isTestDb) {
			this.embededMongo.stop();
		}else {
			mongoClient.close();			
		}
	}

}
