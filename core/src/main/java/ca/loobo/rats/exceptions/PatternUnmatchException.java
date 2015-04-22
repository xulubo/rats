package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class PatternUnmatchException extends RuntimeException {

	public PatternUnmatchException(String msg) {
		super(msg);
	}
}
