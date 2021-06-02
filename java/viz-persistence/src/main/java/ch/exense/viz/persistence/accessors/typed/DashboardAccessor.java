package ch.exense.viz.persistence.accessors.typed;

import step.core.accessors.AbstractAccessor;
import step.core.accessors.Accessor;
import step.core.collections.Collection;
import ch.exense.viz.persistence.model.Dashboard;


public class DashboardAccessor extends AbstractAccessor<Dashboard> implements Accessor<Dashboard> {

	public DashboardAccessor(Collection<Dashboard> collectionDriver) {
		super(collectionDriver);
	}

}
