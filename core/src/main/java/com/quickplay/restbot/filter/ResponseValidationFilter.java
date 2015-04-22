package com.quickplay.restbot.filter;

import java.util.List;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.caze.ParamValue;
import com.quickplay.restbot.caze.TestContext;
import com.quickplay.restbot.exceptions.CaseResponseValidationException;
import com.quickplay.restbot.validators.ValueReader;
import com.quickplay.restbot.validators.ValueValidatorChainFactory;

public class ResponseValidationFilter implements CaseFilter {
	private static Logger logger = LoggerFactory.getLogger(ResponseValidationFilter.class);
	
	@Resource
	TestContext testContext;

	@Resource
	ValueValidatorChainFactory validatorChainFactory;
	
	private static final String NAME = "RESPONSE_VALIDATION";
	
	@Override
	public void filter(Case c, CaseFilterChain chain) {
		logger.debug("Enter ..");
		validateResponse(c);

		chain.doFilter(c);
	}

	@Override
	public String name() {
		return NAME;
	}



	//validate data in response body, data field is indicated in JsonPath
	private void validateResponse(Case c) {
		for (Entry<String, ParamValue> entry : c.getExpectations().entrySet()) {
			String jsonPath = entry.getKey();
			String pattern = entry.getValue().toString();

			//pattern is empty, not need to check
			if (StringUtils.isBlank(pattern)) {
				continue;
			}

			try {
				Object value = testContext.readCaseValue(c, jsonPath);
				// null value means value is absent which is a good situation for some cases
				//if (value != null) {
					validatorChainFactory.newChain().validate(value, pattern);
				//}
				//else {
				//	throw new CaseResponseValidationException("Can't read value by " + jsonPath);					
				//}
			} catch(PatternSyntaxException e) {
				logger.error(e.getMessage(), e);
				throw new CaseResponseValidationException("pattern syntax error pattern=" + pattern, e);
			} catch (Exception e) {
				throw new CaseResponseValidationException("path=" + jsonPath + " " + e.getMessage(), e);
			} 
		}
	}


}
