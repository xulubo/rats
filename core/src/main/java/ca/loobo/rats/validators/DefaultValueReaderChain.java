package ca.loobo.rats.validators;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import ca.loobo.rats.AppContext;
import ca.loobo.rats.caze.Case;
import ca.loobo.rats.exceptions.VariableUnresolvableException;

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
