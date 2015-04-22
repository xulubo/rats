package ca.loobo.rats.reader;

import org.junit.runners.model.InitializationError;
import org.springframework.core.io.Resource;

import ca.loobo.rats.caze.TestContext;

public interface ResourceReader {

	void read(TestContext context, Resource resource) throws InitializationError;

}
