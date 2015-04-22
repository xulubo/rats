package com.quickplay.restbot.validators;

import com.quickplay.restbot.caze.Case;


public interface ValueReaderChain {

	public Object read(Case caze, String valName);
}
