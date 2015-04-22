package ca.loobo.rats.expr;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;

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
