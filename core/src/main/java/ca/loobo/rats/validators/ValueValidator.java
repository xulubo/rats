package ca.loobo.rats.validators;

public interface ValueValidator {

	//validate if the value satisfy the expression
	void validate(final Object value, final String expression, final ValueValidatorChain chain);
}
