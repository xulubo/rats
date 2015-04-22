package ca.loobo.rats.runner;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import ca.loobo.rats.AppContext;
import ca.loobo.rats.annotations.Filter;
import ca.loobo.rats.annotations.Filters;
import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.TestContext;
import ca.loobo.rats.filter.CaseFilter;

public class DefaultCaseRunnerFactory implements CaseRunnerFactory {

	private List<CaseFilter> caseFilters;
	
	@Override
	public CaseRunner newCaseRunner(TestContext context, Case c) {
		CaseRunner r = c.isCollectionCase() ? 
				new CollectionCaseRunner(context, c, caseFilters):
				new SingleCaseRunner(context, c, caseFilters);
		return r;
	}

	public void setCaseFilters(List<CaseFilter> filters) {
		this.caseFilters = filters;
	}
	
	private void add(String name, CaseFilter f, boolean before) {
		AppContext.autowireBean(f);

		for(int i=0; i<caseFilters.size(); i++) {
			if (caseFilters.get(i).name().equals(name)) {
				caseFilters.add(i + (before ? 0 : 1), f);
				return;
			}
		}
		throw new RuntimeException("can't find filter " + name);
	}
	
	@Override
	public void inject(Filters filters) {
		if (filters == null || filters.value() == null || filters.value().length==0) {
			return;
		}
		
		try {
			for(Filter f : filters.value()) {
				if (!StringUtils.isBlank(f.before())) {
					this.add(f.before(), f.value().newInstance(), true);
					continue;
				}
				if (!StringUtils.isBlank(f.after())) {
					this.add(f.after(), f.value().newInstance(), false);
					continue;
				}
			}
		} catch(Throwable t) {
			throw new RuntimeException(t.getMessage(), t);
		}
	}
}
