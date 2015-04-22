package com.quickplay.restbot;

import org.junit.runner.RunWith;

import com.quickplay.restbot.annotations.Host;
import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles(
	value="file:resources/cas_testcases.xlsx"
	, allows = { "TVX-1737:.*|TVX-1741:.*" }
	)
@Host(host="localhost", port=8080)
public class CASTest {

}
