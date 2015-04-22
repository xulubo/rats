package ca.loobo.rats.validators;

import org.springframework.http.ResponseEntity;

import ca.loobo.rats.caze.Case;

public class ResponseHeaderValueReader implements ValueReader {


	@Override
	public Object read(Case caze, String valName, ValueReaderChain chain) {
		if (!accept(valName)) {
			return chain.read(caze, valName);
		}
		
		ResponseEntity<String> response = caze.getResponseEntity();
		String headerName = valName.substring(2);
		if (headerName.equalsIgnoreCase("status")) {
			return response.getStatusCode().value();
		}
		else {
			String headerValue = response.getHeaders().getFirst(headerName);
			return headerValue;
		}
	}

	public boolean accept(String path) {
		return path.startsWith("H.");
	}

}
