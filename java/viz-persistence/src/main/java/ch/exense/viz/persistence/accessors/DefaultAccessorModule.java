/*******************************************************************************
 * (C) Copyright 2016 Jerome Comte and Dorian Cransac
 *  
 * This code is protected by the  GNU Affero General Public License
 * See <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package ch.exense.viz.persistence.accessors;

import com.fasterxml.jackson.databind.module.SimpleModule;

import ch.exense.viz.persistence.accessors.serialization.DottedMapKeyDeserializer;
import ch.exense.viz.persistence.accessors.serialization.DottedMapKeySerializer;

/**
 * Default Jackson module used for the serialization in the persistence layer (Jongo)
 * This module isn't used in the REST layer (Jersey) and can therefore be used to define serializers that only 
 * have to be used when persisting objects
 * 
 */
public class DefaultAccessorModule extends SimpleModule {

	private static final long serialVersionUID = 5544301456563146100L;

	public DefaultAccessorModule() {
		super();
		
		addSerializer(DottedKeyMap.class, new DottedMapKeySerializer());
		addDeserializer(DottedKeyMap.class, new DottedMapKeyDeserializer());
		
	}

}
