package ca.loobo.rats.exceptions;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException(String filePath) {
		super("couldn't read file " + filePath);
	}

}
