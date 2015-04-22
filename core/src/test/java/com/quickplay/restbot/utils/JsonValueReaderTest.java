package com.quickplay.restbot.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.quickplay.restbot.AppContext;
import com.quickplay.restbot.exceptions.ExprUnresolvableException;
import com.quickplay.restbot.expr.ExprEvaluator;
import com.quickplay.restbot.validators.DefaultValueValidator;
import com.quickplay.restbot.validators.JsonResponseValueReader;

public class JsonValueReaderTest {

	@Resource
	ExprEvaluator exprEvaluator;
	
	@Before
	public void init() {
		AppContext.autowireBean(this);
	}
	
	@Test
	public void test() throws IOException {
		File jsonFile = new File("src/test/resources/std_responses/case21_response.json");
		Object value = JsonPath.read(jsonFile, "$..titles.people");

		System.err.println(new Gson().toJson(value));
	}

	@Test
	public void readCollectionData() throws IOException {
		String jsonPath = "$..titles.people/$..name[*]";
		File jsonFile = new File("src/test/resources/std_responses/case21_response.json");

		JsonResponseValueReader reader = new JsonResponseValueReader();
		assertTrue(reader.accept(jsonPath));
		
		Object obj = reader.read(IOUtils.toString(new FileReader(jsonFile)), jsonPath);

		assertTrue(obj instanceof Collection);
		
		for(Object value : (Collection<?>) obj) {
			assertTrue(value instanceof Collection);
		}
		
		DefaultValueValidator validator = new DefaultValueValidator();
		validator.validate(obj, "contains(\"Jennifer Love Hewitt\")", null);
	}

	@Test
	public void shouldSucceedIfCollectionsContainsExpectedValue() throws Exception {

		Object ret = exprEvaluator.evaluate(Arrays.asList("abc", "123"), "contains(\"123\")");
		assertTrue(ret instanceof Boolean);
		assertTrue(ret.equals(true));
	}	
	
	@Test(expected = ExprUnresolvableException.class)
	public void shouldFailIfExpressionIsUnresolvable() throws Exception {
		exprEvaluator.evaluate("abce");
	}		
}
