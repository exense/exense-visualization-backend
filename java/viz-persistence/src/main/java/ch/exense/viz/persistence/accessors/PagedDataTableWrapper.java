package ch.exense.viz.persistence.accessors;

import java.util.List;

public class PagedDataTableWrapper extends DataTableWrapper{

	private int draw;
	private long recordsTotal;
	private long recordsFiltered;

	public PagedDataTableWrapper(List<ObjectWrapper> data) {
		super(data);
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
}
