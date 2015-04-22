package ca.loobo.rats.validators;

import ca.loobo.rats.caze.Case;


public interface ValueReaderChain {

	public Object read(Case caze, String valName);
}
