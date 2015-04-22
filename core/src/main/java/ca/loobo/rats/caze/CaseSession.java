package ca.loobo.rats.caze;

import java.util.Map;

import com.google.common.collect.Maps;

public class CaseSession {

	Case caze;
	private Map<String, Object> attributes = Maps.newHashMap();
	
	public Case getCase() {
		return caze;
	}

	public void setCase(Case caze) {
		this.caze = caze;
	}
	
	public TestContext getContext() {
		return caze.getContext();
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}
	
	public void clear() {
		caze = null;
		attributes.clear();
	}	
}
