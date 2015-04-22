package ca.loobo.rats;

import org.junit.runner.RunWith;

import ca.loobo.rats.annotations.Host;
import ca.loobo.rats.annotations.ResourceFiles;
import ca.loobo.rats.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles(
	value="file:resources/cas_testcases.xlsx"
	, allows = { "TVX-1737:.*|TVX-1741:.*" }
	)
@Host(host="localhost", port=8080)
public class CASTest {

}
