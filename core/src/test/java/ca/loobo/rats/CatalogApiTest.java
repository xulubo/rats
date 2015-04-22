package ca.loobo.rats;

import org.junit.runner.RunWith;

import ca.loobo.rats.annotations.ResourceFiles;
import ca.loobo.rats.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles(
	value="file:resources/new_epg_search_testcases.xlsx"
//	, allows = { ".*_es_18.*" }
	)
//@Host(host="localhost", port=8090)
public class CatalogApiTest {

}
