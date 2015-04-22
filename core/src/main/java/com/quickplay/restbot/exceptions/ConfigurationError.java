package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class ConfigurationError extends Error{

	public ConfigurationError(String msg) {
		super(msg);
	}
}
