package com.quickplay.restbot.validators;


public interface ValueValidatorChain {

	public void validate(Object value, String validationExpression);
}
