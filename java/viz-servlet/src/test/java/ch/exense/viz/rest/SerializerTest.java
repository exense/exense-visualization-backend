package ch.exense.viz.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.exense.viz.persistence.accessors.DataTableWrapper;
import ch.exense.viz.persistence.accessors.ObjectWrapper;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class SerializerTest {
	
	@Test
	public void serializeTest() throws JsonProcessingException {
		List<ObjectWrapper> data = new ArrayList<>();
		Map<String,Object> object = new HashMap<>() ;
		object.put("foo","bar");
		data.add(new ObjectWrapper("a name", object));
		DataTableWrapper wrapper = new DataTableWrapper(data);
		ObjectMapper om = new ObjectMapper();
		//Assert.assertEquals("{\"data\":[{\"foo\":\"bar\"}]}", om.writeValueAsString(wrapper));
		Assert.assertEquals("{\"data\":[[\"a name\"]]}", om.writeValueAsString(wrapper));
	}

}
