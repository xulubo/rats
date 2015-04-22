package com.quickplay.restbot.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.caze.CaseSessionHolder;

@Component
public class SessionInjectionFilter implements CaseFilter {
	final static Logger logger = LoggerFactory.getLogger(SessionInjectionFilter.class);

	@Override
	public void filter(Case c, CaseFilterChain chain) {
		logger.debug("Enter ..");
		CaseSessionHolder.currentSession().setCase(c);

		chain.doFilter(c);
		
		CaseSessionHolder.currentSession().clear();
	}
	
	@Override
	public String name() {
		return "SESSION_INJECTION";
	}	
}
