package com.quickplay.restbot.samples;

import org.junit.runner.RunWith;

import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles(
	value="file:src/test/resources/samplecase.xlsx"
//	, allows = { "case1" }
	)
//@Host(host="localhost", port=8082)
public class SampleTest {

}
