package ca.loobo.rats.runner;

import java.util.List;

import org.junit.runner.Runner;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.filter.CaseFilter;

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
