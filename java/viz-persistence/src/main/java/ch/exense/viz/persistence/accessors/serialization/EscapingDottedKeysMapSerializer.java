/*******************************************************************************
 * (C) Copyright 2016 Jerome Comte and Dorian Cransac
 *  
 * This code is protected by the  GNU Affero General Public License
 * See <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package ch.exense.viz.persistence.accessors.serialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@SuppressWarnings("rawtypes")
public class EscapingDottedKeysMapSerializer extends JsonSerializer<Map> {

	public EscapingDottedKeysMapSerializer() {
		super();
	}

	@Override
	public void serialize(Map value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		Map<Object, Object> newMap = new HashMap<>();
		for(Object key:value.keySet()) {
			newMap.put(encodeKey(key), value.get(key));
		}
		jgen.writeObject(newMap);
	}
	
    // replacing "." and "$" by their unicodes as they are invalid keys in BSON
    private Object encodeKey(Object key) {
    	if(key instanceof String) {
    		return ((String) key).replace("\\\\", "\\\\\\\\").replace("\\$", "\\\\u0024").replace(".", "\\\\u002e");
    	} else {
    		return key;
    	}
    }

}
