package com.quickplay.restbot.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.caze.Case;

@Component
public class MakeReportFilter extends ScriptFilter {

	final static Logger logger = LoggerFactory.getLogger(MakeReportFilter.class);
	
	@Override
	public String name() {
		return "MAKE_REPORT";
	}
	
	@Override
	public void filter(Case c, CaseFilterChain chain) {
		execute(c.getPostScript());
		chain.doFilter(c);
	}
}
