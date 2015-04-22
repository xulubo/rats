package com.quickplay.restbot.validators;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import com.quickplay.restbot.AppContext;
import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.exceptions.VariableUnresolvableException;

public class DefaultValueReaderChain implements ValueReaderChain {

	@Resource
	List<ValueReader> responseValueReaders;
	private Iterator<ValueReader> readerIterator;

	public DefaultValueReaderChain() {
		AppContext.autowireBean(this);
		readerIterator = responseValueReaders.iterator();
	}
	
	@Override
	public Object read(Case caze, String valName) {
		try {
			if (readerIterator.hasNext()) {
				return readerIterator.next().read(caze, valName, this);
			}
		}
		catch(Throwable t) {
			throw new VariableUnresolvableException(t.getMessage(), t);
		}
				
		return null;
	}
}
