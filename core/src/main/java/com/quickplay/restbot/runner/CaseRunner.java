package com.quickplay.restbot.runner;

import java.util.List;

import org.junit.runner.Runner;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.filter.CaseFilter;

public abstract class CaseRunner extends Runner {

	final protected Case mCase;
	private List<CaseFilter> filters;
	
	public CaseRunner(Case c, List<CaseFilter> caseFilters) {
		this.mCase = c;
		this.filters = caseFilters;
	}
	
	public Case getCase() {
		return this.mCase;
	}

	public List<CaseFilter> getFilters() {
		return filters;
	}

}
