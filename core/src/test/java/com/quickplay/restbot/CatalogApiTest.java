package com.quickplay.restbot;

import org.junit.runner.RunWith;

import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles(
	value="file:resources/new_epg_search_testcases.xlsx"
//	, allows = { ".*_es_18.*" }
	)
//@Host(host="localhost", port=8090)
public class CatalogApiTest {

}
