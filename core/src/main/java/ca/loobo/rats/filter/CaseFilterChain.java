package ca.loobo.rats.filter;

import ca.loobo.rats.caze.Case;

public interface CaseFilterChain {

	public void doFilter(Case c);
}
