package ca.loobo.rats.runner;

import javax.annotation.Resource;

import org.junit.runners.model.InitializationError;

import ca.loobo.rats.expr.ExprEvaluator;
import ca.loobo.rats.reader.ExcelReader;

public class SuiteRunner extends AbstractSuiteRunner {

	@Resource
	ExprEvaluator e;
	
	public SuiteRunner(Class<?> testClass)
			throws InitializationError {
		super(testClass, new ExcelReader());

	}

}
