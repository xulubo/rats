package ca.loobo.rats.filter;

import ca.loobo.rats.caze.Case;

public interface CaseFilter {

	void filter(Case c, CaseFilterChain chain);
	String name();
}
