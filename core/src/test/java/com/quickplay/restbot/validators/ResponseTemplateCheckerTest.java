package com.quickplay.restbot.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Test;

import com.quickplay.restbot.filter.ResponseTemplateValidationFilter;

public class ResponseTemplateCheckerTest {

	@Test
	public void assertEqualsTest() throws FileNotFoundException, JSONException, IOException {
		ResponseTemplateValidationFilter checker = new ResponseTemplateValidationFilter();
		File template = new File("src/test/resources/template.json");
		File actual = new File("src/test/resources/actual.json");
		
		checker.assertEquals(
				IOUtils.toString(new FileInputStream(template)),
				IOUtils.toString(new FileInputStream(actual)));
	}
}
