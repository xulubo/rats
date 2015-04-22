package ca.loobo.rats.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;
import ca.loobo.rats.exceptions.ExprAssertionException;

@Component
@ExprHandler
public class ExprAssertionHandler {
	static final Logger logger = LoggerFactory.getLogger(ExprAssertionHandler.class);
	
	@ExprMethod(alias="true")
	public Object assertTrue(Boolean val) {
		if (!val) {
			throw new ExprAssertionException("asserTrue failed");
		}
		
		return true;
	}

	@ExprMethod
	public Object eq(Object arg1, Object arg2) {
		if (!arg1.equals(arg2)) {
			throw new ExprAssertionException("eq: expect " + arg1 + " but got " + arg2);
		}
		
		return true;
	}
	
	@ExprMethod
	public Object ne(Object arg1, Object arg2) {
		if (arg1.equals(arg2)) {
			throw new ExprAssertionException("ne: " + arg1 + " equals to " + arg2);
		}
		
		return true;
	}	
}
