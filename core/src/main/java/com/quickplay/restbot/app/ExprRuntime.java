package com.quickplay.restbot.app;

import com.quickplay.restbot.AppContext;
import com.quickplay.restbot.expr.ExprEvaluator;

public class ExprRuntime {

	public void evaluate(String expr) throws Exception {
		ExprEvaluator ee = AppContext.instance().getApplicationContext().getBean(ExprEvaluator.class);
		ee.evaluate(expr);
	}
}
