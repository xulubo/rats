package com.quickplay.restbot.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.exceptions.CaseParseException;
import com.quickplay.restbot.expr.ExprEvaluator;

@Component
public abstract class ScriptFilter implements CaseFilter {

	final static Logger logger = LoggerFactory.getLogger(ScriptFilter.class);

	@Resource
	ExprEvaluator exprEvaluator;

	public Object execute(String script) {
		Object ret = null;
		if (StringUtils.isBlank(script)) {
			return ret;
		}
		
		BufferedReader reader = new BufferedReader(new StringReader(script));
		String expr = null;
		try {
			while((expr=reader.readLine()) != null) {
				logger.debug("parsing {} ", expr);
				ret = exprEvaluator.evaluate(expr);				
			}
		} catch (IOException e) {
			throw new CaseParseException("Couldn't read post script", e);
		} catch (Exception e) {
			logger.error("caught exception " + e.getMessage());
			throw new RuntimeException("failed to evaluate " + expr + ": " + e.getMessage(), e);
		}

		return ret;
	}
}
