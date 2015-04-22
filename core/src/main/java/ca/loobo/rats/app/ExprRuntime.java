package ca.loobo.rats.app;

import ca.loobo.rats.AppContext;
import ca.loobo.rats.expr.ExprEvaluator;

public class ExprRuntime {

	public void evaluate(String expr) throws Exception {
		ExprEvaluator ee = AppContext.instance().getApplicationContext().getBean(ExprEvaluator.class);
		ee.evaluate(expr);
	}
}
