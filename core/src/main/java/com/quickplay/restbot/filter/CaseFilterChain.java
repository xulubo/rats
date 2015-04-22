package com.quickplay.restbot.filter;

import com.quickplay.restbot.caze.Case;

public interface CaseFilterChain {

	public void doFilter(Case c);
}
