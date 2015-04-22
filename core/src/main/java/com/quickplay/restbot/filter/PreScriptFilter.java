package com.quickplay.restbot.filter;

import org.springframework.stereotype.Component;

import com.quickplay.restbot.caze.Case;

@Component
public class PreScriptFilter extends ScriptFilter {

	@Override
	public String name() {
		return "PRE_SCRIPT";
	}
	
	@Override
	public void filter(Case c, CaseFilterChain chain) {
		execute(c.getPreScript());
		chain.doFilter(c);
	}
}
