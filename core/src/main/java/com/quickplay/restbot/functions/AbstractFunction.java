package com.quickplay.restbot.functions;

abstract public class AbstractFunction implements Function {

	public String call(String methodName, String param)
	{
		return call(methodName, new String[]{param});
	}

}
