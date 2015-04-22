package com.quickplay.restbot.runner;

import javax.annotation.Resource;

import org.junit.runners.model.InitializationError;

import com.quickplay.restbot.expr.ExprEvaluator;
import com.quickplay.restbot.reader.ExcelReader;

public class SuiteRunner extends AbstractSuiteRunner {

	@Resource
	ExprEvaluator e;
	
	public SuiteRunner(Class<?> testClass)
			throws InitializationError {
		super(testClass, new ExcelReader());

	}

}
