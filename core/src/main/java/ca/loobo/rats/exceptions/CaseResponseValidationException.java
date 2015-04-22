package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class CaseResponseValidationException extends RuntimeException {

	public CaseResponseValidationException(String msg) {
		super(msg);
	}

	public CaseResponseValidationException(String msg, Throwable t) {
		super(msg, t);
	}
}
