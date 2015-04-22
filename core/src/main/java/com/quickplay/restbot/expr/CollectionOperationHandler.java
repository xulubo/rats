package com.quickplay.restbot.expr;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.annotations.ExprHandler;
import com.quickplay.restbot.annotations.ExprMethod;

@Component
@ExprHandler
public class CollectionOperationHandler {
	static final Logger logger = LoggerFactory.getLogger(CollectionOperationHandler.class);
	
	@ExprMethod
	public Boolean contains(Collection<?>values, String pattern) {
		for(Object val : values) {
			if (val.toString().matches(pattern)) {
				return true;
			}
		}
		
		return false;
	}

}
