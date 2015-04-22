package com.quickplay.restbot.exceptions;

@SuppressWarnings("serial")
public class ExprEvaluationException extends RuntimeException {

	public ExprEvaluationException(String msg) {
		super(msg);
	}
	
	public ExprEvaluationException(Throwable t) {
		super(t);
	}
}
