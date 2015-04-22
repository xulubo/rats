package com.quickplay.restbot.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.annotations.ExprHandler;
import com.quickplay.restbot.annotations.ExprMethod;
import com.quickplay.restbot.exceptions.ExprMethodExecutionException;

@Component
@ExprHandler
public class ExprUtilFunctionHandler {
	static final Logger logger = LoggerFactory.getLogger(ExprUtilFunctionHandler.class);
	
	@ExprMethod
	public Boolean sleep(Object millis) {
		try {
			Thread.sleep(Long.valueOf(millis.toString()));
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new ExprMethodExecutionException(e.getMessage());
		} 
		
		return true;
	}
}
