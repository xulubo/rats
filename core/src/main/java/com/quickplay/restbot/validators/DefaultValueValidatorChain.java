package com.quickplay.restbot.validators;

import java.util.Iterator;

import com.quickplay.restbot.exceptions.VariableUnresolvableException;

public class DefaultValueValidatorChain implements ValueValidatorChain {

	private Iterator<ValueValidator> validatorIterator;
	
	public DefaultValueValidatorChain(Iterator<ValueValidator> iter) {
		this.validatorIterator = iter;
	}
	
	@Override
	public void validate(Object value, String validationExpression) {
		try {
			if (validatorIterator.hasNext()) {
				validatorIterator.next().validate(value, validationExpression, this);
			}
		}
		catch(Throwable t) {
			throw new VariableUnresolvableException(t.getMessage(), t);
		}
		
	}
}
