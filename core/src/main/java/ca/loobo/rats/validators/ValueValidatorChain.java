package ca.loobo.rats.validators;


public interface ValueValidatorChain {

	public void validate(Object value, String validationExpression);
}
