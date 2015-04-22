package ca.loobo.rats.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;
import ca.loobo.rats.exceptions.ExprMethodExecutionException;

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
