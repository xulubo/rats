package com.quickplay.restbot;

import org.junit.runner.RunWith;

import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles("file:src/main/resources/solr_resources/testcases.xlsx")
public class SolrTest {

}
