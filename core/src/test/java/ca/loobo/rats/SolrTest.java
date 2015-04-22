package ca.loobo.rats;

import org.junit.runner.RunWith;

import ca.loobo.rats.annotations.ResourceFiles;
import ca.loobo.rats.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles("file:src/main/resources/solr_resources/testcases.xlsx")
public class SolrTest {

}
