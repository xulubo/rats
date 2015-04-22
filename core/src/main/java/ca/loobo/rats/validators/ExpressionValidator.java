package ca.loobo.rats.validators;

import javax.annotation.Resource;

import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.loobo.rats.exceptions.ExprMethodNotFoundException;
import ca.loobo.rats.exceptions.ExprUnresolvableException;
import ca.loobo.rats.expr.ExprEvaluator;


@Component
public class ExpressionValidator implements ValueValidator {
	private static final Logger logger = LoggerFactory.getLogger(ExpressionValidator.class);
	
	@Resource
	ExprEvaluator exprEvaluator;
	
	@Override
	public void validate(Object value, final String expression,
			ValueValidatorChain chain) {
		try {
			String str = expression;
			// value.expression
			if (expression.startsWith(".")) {
				str = expression.substring(1);
			}
			assertEquals(true, exprEvaluator.evaluate(value, str));
		} catch (ExprUnresolvableException | ExprMethodNotFoundException e) {
			chain.validate(value, expression);
		} catch (Exception e) {
			logger.error("unexpected exception", e);
			chain.validate(value, expression);
		}
	}
}
