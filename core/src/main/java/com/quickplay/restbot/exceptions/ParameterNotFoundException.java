package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class ParameterNotFoundException extends RuntimeException {

	public ParameterNotFoundException(String paramName) {
		super(paramName + " couldn't be found");
	}

}
