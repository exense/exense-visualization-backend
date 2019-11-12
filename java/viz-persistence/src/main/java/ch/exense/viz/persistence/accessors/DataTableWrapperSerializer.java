package ch.exense.viz.persistence.accessors;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DataTableWrapperSerializer extends JsonSerializer<DataTableWrapper> {

	@Override
	public void serialize(DataTableWrapper wrapper, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
	    
		gen.writeStartObject();
		
	       gen.writeArrayFieldStart("data");
	        List<ObjectWrapper> objs = wrapper.getData();
	        for (ObjectWrapper obj : objs) {
	        	gen.writeStartArray();
	        	gen.writeString(obj.getName());
	    		gen.writeEndArray();
	    		
	        }
	        gen.writeEndArray();
	        
	        gen.writeEndObject();
	        
	}

}
