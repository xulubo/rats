package com.quickplay.restbot.reader;

import org.junit.runners.model.InitializationError;
import org.springframework.core.io.Resource;

import com.quickplay.restbot.caze.TestContext;

public interface ResourceReader {

	void read(TestContext context, Resource resource) throws InitializationError;

}
