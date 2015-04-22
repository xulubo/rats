package com.quickplay.restbot.runner;

import com.quickplay.restbot.annotations.Filters;
import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.caze.TestContext;

public interface CaseRunnerFactory {

	public CaseRunner newCaseRunner(TestContext context, Case c);
	public void inject(Filters filters);

}
