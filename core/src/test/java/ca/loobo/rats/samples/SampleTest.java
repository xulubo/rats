package ca.loobo.rats.samples;

import org.junit.runner.RunWith;

import ca.loobo.rats.annotations.ResourceFiles;
import ca.loobo.rats.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles(
	value="file:src/test/resources/samplecase.xlsx"
//	, allows = { "case1" }
	)
//@Host(host="localhost", port=8082)
public class SampleTest {

}
