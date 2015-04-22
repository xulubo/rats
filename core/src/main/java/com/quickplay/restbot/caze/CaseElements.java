package com.quickplay.restbot.caze;

import java.util.Map;

public interface CaseElements {

	public Map<String, String> getQueryParams();
	public Map<String, String> getMetas();
	public Map<String, String> getExpectations();
	public Map<String, String> getPathVariables();
}
