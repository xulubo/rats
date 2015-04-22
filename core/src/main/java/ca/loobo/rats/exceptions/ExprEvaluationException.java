package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class ExprEvaluationException extends RuntimeException {

	public ExprEvaluationException(String msg) {
		super(msg);
	}
	
	public ExprEvaluationException(Throwable t) {
		super(t);
	}
}
