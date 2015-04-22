package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class CaseParseException extends RuntimeException {

	public CaseParseException(String msg, Throwable t) {
		super(msg, t);
	}
}
