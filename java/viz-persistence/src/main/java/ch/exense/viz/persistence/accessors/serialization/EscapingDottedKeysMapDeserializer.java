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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings("rawtypes")
public class EscapingDottedKeysMapDeserializer extends JsonDeserializer<Map> {

	private ObjectMapper mapper;
	
    public EscapingDottedKeysMapDeserializer() {
		super();
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
	}

	private String decodeKey(String key) {
        return key.replace("\\\\u002e", ".").replace("\\\\u0024", "\\$").replace("\\\\\\\\", "\\\\");
    }

	@Override
	public Map deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.readValueAsTree();
		
		Map result = new HashMap<>();
		ObjectNode o = (ObjectNode) node;
        o.fields().forEachRemaining(e -> {
        	String key = e.getKey();
        	JsonNode eNode = e.getValue();
        	try {
        		result.put(decodeKey(key), mapper.treeToValue(eNode, Object.class));
        	} catch (Throwable ex) {
        		// Ignore these exception as it can be a ClassNotFoundException
        	}
        });
		
		return result;
	}
}
