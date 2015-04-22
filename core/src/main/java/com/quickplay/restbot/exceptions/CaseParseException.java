package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class CaseParseException extends RuntimeException {

	public CaseParseException(String msg, Throwable t) {
		super(msg, t);
	}
}
