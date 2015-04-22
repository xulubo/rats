package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class ExprUnresolvableException extends RuntimeException {

	public ExprUnresolvableException(String msg) {
		super(msg);
	}
	
	public ExprUnresolvableException(String msg, Throwable t) {
		super(msg, t);
	}
}
