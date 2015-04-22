package ca.loobo.rats.param.resolver;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ca.loobo.rats.exceptions.ExprUnresolvableException;
import ca.loobo.rats.exceptions.VariableUnresolvableException;
import ca.loobo.rats.expr.ExprEvaluator;

/**
 * Check if the value is an resolvable expression, evaluate it if it does
 * @author robertx
 *
 */
@Component
public class ExprValueResolver implements ValueResolver {
	@Resource
	ExprEvaluator exprEvaluator;

	
	@Override
	public String resolve(String value, ValueResolverChain chain) {
		try {
			Object exprValue = exprEvaluator.evaluate(value);
			if (exprValue != null) {
				value = exprValue.toString();
			}
		} catch(ExprUnresolvableException e) {
			//ignore
		} catch(Exception e) {
			//e.printStackTrace();
			throw new VariableUnresolvableException("couldn't resolve value " + value, e);
		}
		
		return chain.resolve(value);
	}

}
