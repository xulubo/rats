package ca.loobo.rats.runner;

import ca.loobo.rats.annotations.Filters;
import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.TestContext;

public interface CaseRunnerFactory {

	public CaseRunner newCaseRunner(TestContext context, Case c);
	public void inject(Filters filters);

}
