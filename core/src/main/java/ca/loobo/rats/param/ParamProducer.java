package ca.loobo.rats.param;

import ca.loobo.rats.caze.TestContext;

public interface ParamProducer {

	public String value(TestContext context, String valueUrl);
}
