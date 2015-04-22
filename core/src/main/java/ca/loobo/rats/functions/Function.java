package ca.loobo.rats.functions;

public interface Function {

	public String call(String methodName, String param);
	
	public String call(String methodName, String[] params);

}
