package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class PatternUnmatchException extends RuntimeException {

	public PatternUnmatchException(String msg) {
		super(msg);
	}
}
