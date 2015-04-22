package com.quickplay.restbot.validators;

import com.quickplay.restbot.caze.Case;

public class DefaultValueReader implements ValueReader {

	@Override
	public Object read(Case caze, String name, ValueReaderChain chain) {
		
		String value;
		
		//check if it is a path variable
		value = caze.getPathVariable(name);
		if (value != null) {
			return value;
		}
		
		//check if it is a query parameter
		value = caze.getQueryParameter(name).toString();
		if (value != null) {
			return value;
		}
		
		//check if it is a context parameter
		value = caze.getContext().getParamMap().get(name);
		if (value != null) {
			return value;
		}
		
		return chain.read(caze, name);
	}
}
