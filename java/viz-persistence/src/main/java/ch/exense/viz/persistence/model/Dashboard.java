package ch.exense.viz.persistence.model;

import java.util.List;

import ch.exense.viz.persistence.accessors.AbstractIdentifiableObject;

public class Dashboard extends AbstractIdentifiableObject{

	private String title;
	
	private String oid;
	
	private List<Object> widgets;
	
	private ManagerState mgrstate;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public List<Object> getWidgets() {
		return widgets;
	}
	public void setWidgets(List<Object> widgets) {
		this.widgets = widgets;
	}
	public ManagerState getMgrstate() {
		return mgrstate;
	}
	public void setMgrstate(ManagerState mgrstate) {
		this.mgrstate = mgrstate;
	}
	
	public String toString() {
		return "_id:"+super.getId()+"title:"+this.title+"; oid:"+ oid+"; widgets:"+ widgets+"; mgrstate:"+mgrstate;  
	}
}
