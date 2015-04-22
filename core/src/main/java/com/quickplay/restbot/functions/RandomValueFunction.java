package com.quickplay.restbot.functions;

import org.apache.commons.lang.RandomStringUtils;

public class RandomValueFunction extends AbstractFunction {

	@Override
	public String call(String method, String[] params) {
		int length = Integer.parseInt(params[0]);

		String value = "";
		if ("rands".equals(method)) {
			value = RandomStringUtils.randomAlphanumeric(length);
		}
		else if ("randi".equals(method)) {
			value = RandomStringUtils.randomNumeric(length);
		}
		
		return value;
	}

}
