package com.quickplay.restbot.filter;

import static com.quickplay.restbot.caze.MetaName.resourcefolder;
import static com.quickplay.restbot.caze.MetaName.responsetemplate;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.exceptions.CaseResponseValidationException;
import com.quickplay.restbot.exceptions.ResourceNotFoundException;
import com.quickplay.restbot.exceptions.ResourceNotReadableException;
import com.quickplay.restbot.json.FieldIgnorableComparator;

public class ResponseTemplateValidationFilter implements CaseFilter {
	private static final Logger logger = LoggerFactory.getLogger(ResponseTemplateValidationFilter.class);
	public static final String NAME = "TEMPLATE_VALIDATION";
	private final FieldIgnorableComparator comparator = new FieldIgnorableComparator(JSONCompareMode.NON_EXTENSIBLE);
	
	@Override
	public void filter(Case c, CaseFilterChain chain) {

		validate(c);

		chain.doFilter(c);
	}
	
	@Override
	public String name() {
		return NAME;
	}	
	
	public void validate(Case c) {
		String template = getResponseTemplate(c);
		if (template == null) {
			logger.trace("template is not available, ignore");
			return;
		}
		
		try {
			assertEquals(template, c.getResponseBody());	
		} catch(Throwable e) {
			throw new CaseResponseValidationException("template assert error:" + e.getMessage(), e);
		}

	}

	public void assertEquals(String expected, String actual) throws JSONException {
//		net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual);	
		JSONAssert.assertEquals(expected, actual, comparator);
	}
	
	private String getResponseTemplate(Case c) {
		String resourceFolder = c.getMeta(resourcefolder);
		Resource templateResource;
		String templatePath = c.getMeta(responsetemplate);
		if (StringUtils.isNotBlank(templatePath) && StringUtils.isNotBlank(resourceFolder)) {
			templatePath = resourceFolder + "/" + templatePath;
		}
		
		//Check if default response template file exists in resource folder if it is not defined in test case
		//Default response template file has name in the format of template_<CASE ID>.json
		if (StringUtils.isBlank(templatePath)) {
			try {
				templateResource = c.getContext().getResource("template_" + c.getId() + ".json");
				logger.debug("found response template {}", templateResource.getFilename());
			} catch(ResourceNotFoundException e) {
				return null;
			}
		}
		else {
			templateResource = c.getContext().getResource(templatePath);
			if (templateResource == null || !templateResource.isReadable()) {
				throw new ResourceNotReadableException(templatePath);
			}
		}
		

		try {
			return IOUtils.toString(templateResource.getInputStream(), "UTF-8");
		} catch (IOException e) {
			throw new ResourceNotReadableException(templateResource.getFilename());
		}
	}	
}
