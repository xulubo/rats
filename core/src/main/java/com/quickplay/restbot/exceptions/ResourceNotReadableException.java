package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class ResourceNotReadableException extends RuntimeException {

	public ResourceNotReadableException(String filePath) {
		super("couldn't read file " + filePath);
	}
}
