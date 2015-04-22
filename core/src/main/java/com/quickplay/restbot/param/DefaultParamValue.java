package com.quickplay.restbot.param;

import com.quickplay.restbot.caze.ParamValue;
import com.quickplay.restbot.param.resolver.DefaultValueResolverChain;

public class DefaultParamValue implements ParamValue {

	private final String expression;
	private String resolvedString;
	
	public DefaultParamValue() {
		expression = null;
	}
	
	public DefaultParamValue(String expression) {
		this.expression = expression;
	}

	@Override
	public String value() {
		return expression;
	}

	@Override
	public String resolve() {
		if (expression == null) {
			return null;
		}
		
		if (resolvedString == null) {
			resolvedString = new DefaultValueResolverChain().resolve(expression);
		}
		return resolvedString;
	}
	
	@Override
	public String toString() {
		return resolve();
	}
}