package ca.loobo.rats.functions;

abstract public class AbstractFunction implements Function {

	public String call(String methodName, String param)
	{
		return call(methodName, new String[]{param});
	}

}
