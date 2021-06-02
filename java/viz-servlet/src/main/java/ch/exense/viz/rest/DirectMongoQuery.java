package ch.exense.viz.rest;

import step.core.collections.Filter;
import step.core.collections.SearchOrder;

public class DirectMongoQuery {
	
	private String host;
	private String user;
	private int port;
	private String database;
	private String collection;
	private Filter query;
	private SearchOrder order;
	private String projection;
	private int skip;
	private int limit;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public Filter getQuery() {
		return query;
	}
	public void setQuery(Filter query) {
		this.query = query;
	}
	public SearchOrder getSort() {
		return order;
	}
	public void setSort(SearchOrder order) {
		this.order = order;
	}
	public int getSkip() {
		return skip;
	}
	public void setSkip(int skip) {
		this.skip = skip;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getProjection() {
		return projection;
	}
	public void setProjection(String projection) {
		this.projection = projection;
	}
}
