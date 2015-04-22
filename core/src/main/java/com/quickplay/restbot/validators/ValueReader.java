package com.quickplay.restbot.validators;

import com.quickplay.restbot.caze.Case;


public interface ValueReader {

	Object read(Case caze, String path, ValueReaderChain chain);
	
}
