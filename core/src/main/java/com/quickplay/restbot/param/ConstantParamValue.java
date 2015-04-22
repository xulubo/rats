package com.quickplay.restbot.param;

import com.quickplay.restbot.caze.ParamValue;

/**
 * Parameter value is not changeable
 * @author robertx
 *
 */
public class ConstantParamValue implements ParamValue {

	private final String value;
	
	public ConstantParamValue(String value) {
		this.value = value;
	}
	
	@Override
	public String value() {
		return value;
	}

	@Override
	public String resolve() {
		return value;
	}

	@Override
	public String toString() {
		return resolve();
	}
}
