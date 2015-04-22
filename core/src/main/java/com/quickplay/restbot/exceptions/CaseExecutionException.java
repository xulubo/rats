package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class CaseExecutionException extends RuntimeException {

	public CaseExecutionException(String msg) {
		super(msg);
	}
	
	public CaseExecutionException(String msg, Throwable t) {
		super(msg, t);
	}
}
