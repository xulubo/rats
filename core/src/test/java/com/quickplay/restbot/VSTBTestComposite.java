package com.quickplay.restbot;

import org.junit.runner.RunWith;

import com.quickplay.restbot.annotations.Host;
import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.runner.SuiteRunner;

@RunWith(SuiteRunner.class)
@ResourceFiles("vstb_resources/testcases_composite.xlsx")
@Host(host = "54.200.144.190", port = 10101)
public class VSTBTestComposite {

}
