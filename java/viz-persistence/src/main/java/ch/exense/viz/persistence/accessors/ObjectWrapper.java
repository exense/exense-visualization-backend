package ch.exense.viz.persistence.accessors;

import step.core.accessors.AbstractIdentifiableObject;

import java.util.Map;

public class ObjectWrapper extends AbstractIdentifiableObject {
	
	private String name;

	private Map<String, Object> object;

	public ObjectWrapper() {
	}
	
	public ObjectWrapper(String name, Map<String, Object> object) {
		this.name = name;
		this.object = object;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getObject() {
		return object;
	}
	public void setObject(Map<String, Object> object) {
		this.object = object;
	}

}
