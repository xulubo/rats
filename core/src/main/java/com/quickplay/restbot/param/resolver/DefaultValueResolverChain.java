package com.quickplay.restbot.param.resolver;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import com.quickplay.restbot.AppContext;
import com.quickplay.restbot.exceptions.VariableUnresolvableException;

public class DefaultValueResolverChain implements ValueResolverChain {

	@Resource
	List<ValueResolver> valueResolvers;
	
	private Iterator<ValueResolver> resolverIterator;
	
	public DefaultValueResolverChain() {
		AppContext.autowireBean(this);
		this.resolverIterator = valueResolvers.iterator();
	}
	
	@Override
	public String resolve(String value) {
		try {
			if (resolverIterator.hasNext()) {
				value = resolverIterator.next().resolve(value, this);
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new VariableUnresolvableException(t.getMessage(), t);
		}
		
		return value;
	}
}
