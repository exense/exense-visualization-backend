package ch.exense.viz.persistence.accessors.typed;

import ch.exense.viz.persistence.MongoClientSession;
import ch.exense.viz.persistence.accessors.AbstractCRUDAccessor;
import ch.exense.viz.persistence.model.Dashboard;

public class DashboardAccessor extends AbstractCRUDAccessor<Dashboard>{

	public DashboardAccessor(MongoClientSession clientSession) {
		super(clientSession, "dashboards", Dashboard.class);
	}

}
