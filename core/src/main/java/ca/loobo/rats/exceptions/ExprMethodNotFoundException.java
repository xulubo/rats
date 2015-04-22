package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class ExprMethodNotFoundException extends RuntimeException {

	public ExprMethodNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public ExprMethodNotFoundException(String msg) {
		super(msg);
	}
	
	public ExprMethodNotFoundException(Throwable t) {
		super(t);
	}
}
