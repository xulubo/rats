package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class ExprMethodExecutionException extends RuntimeException {

	public ExprMethodExecutionException(String msg) {
		super(msg);
	}
	
	public ExprMethodExecutionException(Throwable t) {
		super(t);
	}
}
