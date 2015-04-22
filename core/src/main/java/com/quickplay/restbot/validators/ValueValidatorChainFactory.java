package com.quickplay.restbot.validators;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class ValueValidatorChainFactory {

	@Resource
	List<ValueValidator> valueValidators;
	
	public ValueValidatorChain newChain() {
		return new DefaultValueValidatorChain(valueValidators.iterator());
	}
}
