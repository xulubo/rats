package com.quickplay.restbot.expr;

import java.util.LinkedList;
import java.util.List;

public class MethodExpr {

	private String instanceName;
	private String methodName;
	private List<Object> parameters = new LinkedList<Object>();

	public String getInstanceName() {
		return this.instanceName;
	}
	
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void addParameter(Object param) {
		this.parameters.add(param);
	}

	public List<Object> getParameters() {
		return parameters;
	}

	
}
