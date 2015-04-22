package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class VariableUnresolvableException extends RuntimeException {

	public VariableUnresolvableException(String msg) {
		super(msg);
	}

	public VariableUnresolvableException(String msg, Throwable t) {
		super(msg, t);
	}
}
