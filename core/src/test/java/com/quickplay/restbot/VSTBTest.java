package com.quickplay.restbot;

import org.junit.runner.RunWith;

import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles({
	"vstb_resources/testcases.xlsx", 
	"vstb_resources/testcases_composite.xlsx"})
public class VSTBTest {

}
