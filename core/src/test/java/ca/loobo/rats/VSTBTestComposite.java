package ca.loobo.rats;

import org.junit.runner.RunWith;

import ca.loobo.rats.annotations.Host;
import ca.loobo.rats.annotations.ResourceFiles;
import ca.loobo.rats.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles("vstb_resources/testcases_composite.xlsx")
@Host(host = "54.200.144.190", port = 10101)
public class VSTBTestComposite {

}
