package ch.exense.viz.persistence.accessors.typed;

import ch.exense.viz.persistence.accessors.AbstractCRUDAccessor;
import ch.exense.viz.persistence.model.Dashboard;
import ch.exense.viz.persistence.mongodb.MongoClientSession;

public class DashboardAccessor extends AbstractCRUDAccessor<Dashboard>{

	public DashboardAccessor(MongoClientSession clientSession) {
		super(clientSession, "dashboards", Dashboard.class);
	}

}
