package com.quickplay.restbot.param;

import com.quickplay.restbot.caze.TestContext;

public interface ParamProducer {

	public String value(TestContext context, String valueUrl);
}
