/*******************************************************************************
 * (C) Copyright 2016 Jerome Comte and Dorian Cransac
 *  
 * This code is protected by the  GNU Affero General Public License
 * See <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package ch.exense.viz.persistence.accessors.serialization;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

// Used to deserialize Map<String, Object>. Per default jackson deserialize the map values as Map
public class MapSerializer extends JsonSerializer<Map<String, Object>> {

	private ObjectMapper mapper;
	
	public MapSerializer() {
		super();
		mapper = MapDeserializer.getMapper();
	}

	@Override
	public void serialize(Map<String, Object> value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		mapper.writeValue(gen, value);
	}
}
