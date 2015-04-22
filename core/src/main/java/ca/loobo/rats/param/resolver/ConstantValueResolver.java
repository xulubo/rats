package ca.loobo.rats.param.resolver;

import org.springframework.util.StringUtils;

/**
 * Used to determine a constant value which will not be resolved
 * A constant value starts with a backslash (will be stripped after this)
 * @author robertx
 *
 */
public class ConstantValueResolver implements ValueResolver {

	@Override
	public String resolve(String value, ValueResolverChain chain) {
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		
		if (value.startsWith("\\")) {
			return value.substring(1);
		}
		
		return chain.resolve(value);
	}

}
