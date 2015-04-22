package ca.loobo.rats;

import org.junit.runner.RunWith;

import ca.loobo.rats.annotations.ResourceFiles;
import ca.loobo.rats.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles({
	"vstb_resources/testcases.xlsx", 
	"vstb_resources/testcases_composite.xlsx"})
public class VSTBTest {

}
