package com.quickplay.restbot.filter;

import com.quickplay.restbot.caze.Case;

public interface CaseFilter {

	void filter(Case c, CaseFilterChain chain);
	String name();
}
