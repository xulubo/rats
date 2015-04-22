package ca.loobo.rats.validators;

import java.util.Iterator;

import ca.loobo.rats.exceptions.VariableUnresolvableException;

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
