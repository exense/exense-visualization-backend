package ch.exense.viz.persistence.accessors;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = DataTableWrapperSerializer.class)
public class DataTableWrapper {
	
	private List<ObjectWrapper> data;

	public DataTableWrapper(List<ObjectWrapper> data) {
		this.data = data;
	}

	public List<ObjectWrapper> getData() {
		return data;
	}

	public void setData(List<ObjectWrapper> data) {
		this.data = data;
	}

}
