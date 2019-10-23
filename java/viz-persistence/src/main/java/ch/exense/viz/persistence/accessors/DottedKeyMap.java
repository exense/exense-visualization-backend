/*******************************************************************************
 * (C) Copyright 2016 Jerome Comte and Dorian Cransac
 *  
 * This code is protected by the  GNU Affero General Public License
 * See <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package ch.exense.viz.persistence.accessors;

import java.util.HashMap;

import ch.exense.viz.persistence.accessors.serialization.DottedMapKeySerializer;

/**
 * 
 * A special Map that is serialized by {@link DottedMapKeySerializer}
 * when persisted in the DB. This serializer supports the persistence of keys
 * that contain "." and "$" which are normally not allowed as key by Mongo.
 * 
 * 
 */
public class DottedKeyMap<K, V>  extends HashMap<K, V> {

	private static final long serialVersionUID = 8922169005470741941L;

}
