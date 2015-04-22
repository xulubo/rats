package ca.loobo.rats.validators;

import java.util.Collection;
import java.util.List;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.exceptions.CaseResponseValidationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;

public class JsonResponseValueReader implements ValueReader {


	@Override
	public Object read(Case caze, String jsonPath, ValueReaderChain chain) {
		if (accept(jsonPath)) {
			return read(caze.getResponseEntity().getBody(), jsonPath);
		}
		return chain.read(caze, jsonPath);
	}
	
	public Object read(String jsonData, String jsonPath) {
		if (jsonData == null) {
			return null;
		}
		
		String parts[] = jsonPath.split("/", 2);
		if (parts.length == 1) {
			return JsonPath.read(jsonData, jsonPath);
		}
		
		String arrayPath = parts[0];
		String elementPath = parts[1];
		Object array = JsonPath.read(jsonData, arrayPath);
		if (!(array instanceof Collection)) {
			throw new CaseResponseValidationException("Object type is not a collection " + arrayPath);
		}
		
		List<Object> list = Lists.newLinkedList();
		for(Object elements : (Collection<?>)array) {
			try {
				String elementJson = new ObjectMapper().writeValueAsString(elements);
				Object value = read(elementJson, elementPath);
				list.add(value);
			} catch (JsonProcessingException e) {
				throw new CaseResponseValidationException(e.getMessage(), e);
			}
		}
		
		return list;
	}

	public boolean accept(String path) {
		return path.startsWith("$.");
	}

}
