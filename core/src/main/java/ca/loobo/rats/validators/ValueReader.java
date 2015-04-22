package ca.loobo.rats.validators;

import ca.loobo.rats.caze.Case;


public interface ValueReader {

	Object read(Case caze, String path, ValueReaderChain chain);
	
}
