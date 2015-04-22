package com.quickplay.restbot.param.resolver;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.quickplay.restbot.exceptions.ExprUnresolvableException;
import com.quickplay.restbot.exceptions.VariableUnresolvableException;
import com.quickplay.restbot.expr.ExprEvaluator;

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
