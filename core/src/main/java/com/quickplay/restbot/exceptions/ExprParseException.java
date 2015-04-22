package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class ExprParseException extends RuntimeException {

	public ExprParseException(String msg) {
		super(msg);
	}
	
	public ExprParseException(Throwable t) {
		super(t);
	}
}
