package com.quickplay.restbot.validators;

import java.util.Collection;

import org.json.JSONArray;

import com.quickplay.restbot.exceptions.CaseResponseValidationException;
import com.quickplay.restbot.exceptions.PatternUnmatchException;

/**
 * Validator if the value matches the provided pattern
 * @author robertx
 *
 */
public class DefaultValueValidator implements ValueValidator {

	@Override
	public void validate(Object value, String valuePattern,	ValueValidatorChain chain) {
		
		// must it be absent ?
		if (ValuePattern.mustAbsent(valuePattern)) {
			if (value != null) {
				if (!shouldReturnArray(valuePattern) || !isEmptyArray(value)) {
					String info = String.format(
							"    expect: absent got: %s", value);
					throw new CaseResponseValidationException(info);
				}
			}

			// Good, the value is absent, no need future check
			return;
		}

		if (ValuePattern.mustExist(valuePattern)) {
			if (value == null || (shouldReturnArray(valuePattern) && isEmptyArray(value))) {
				String info = String.format(
						"    expect: existent but: absent");
				throw new CaseResponseValidationException(info);
			}

			// Good, the value exists
		}
		
		try {
			if (value instanceof Collection) {
				validate((Collection<?>)value, valuePattern);
			} else {
				validate(value, valuePattern);
			}
		} catch(PatternUnmatchException e) {
			if (chain != null) {
				chain.validate(value, valuePattern);
			}
		}
	}


	private boolean shouldReturnArray(String jsonPath) {
		return jsonPath.contains("..");
	}
	

	private boolean isEmptyArray(Object val) {
		return val != null && val instanceof JSONArray && ((JSONArray)val).length()==0;
	}


	
	@SuppressWarnings("unchecked")
	protected void validate(Collection<?> values, String expectedValuePattern) {
		for (Object v : (Collection<Object>) values) {
			if (v instanceof Collection) {
				validate((Collection<?>)v, expectedValuePattern);
				return;
			}
			
			if (!v.toString().matches(expectedValuePattern)) {
				String info = String.format(
						"    expected: [%s] got: [%s]", 
						expectedValuePattern, v == null ? "NULL" : v);
				throw new CaseResponseValidationException(info);
			}
		}
	}

	
	private void validate(Object value, String expectedValuePattern) {
		if (expectedValuePattern == null) {
			throw new CaseResponseValidationException("expectedValuePattern is null");
		}
		
		if (value == null) {
			String info = String.format(
					"    expected: [%s] got: null", expectedValuePattern);
			throw new CaseResponseValidationException(info);
		}
		
		if (!value.toString().matches("^"+expectedValuePattern+"$")) {
			String info = String.format(
					"    expected: [%s] got: [%s]", expectedValuePattern, value);
			throw new CaseResponseValidationException(info);
		}
	}
}
