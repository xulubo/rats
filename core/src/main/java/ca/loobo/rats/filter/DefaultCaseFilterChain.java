package ca.loobo.rats.filter;

import java.util.Iterator;
import java.util.LinkedList;

import ca.loobo.rats.caze.Case;

public class DefaultCaseFilterChain implements CaseFilterChain {

	LinkedList<CaseFilter> filters = new LinkedList<CaseFilter>();
	Iterator<CaseFilter> filterIter;
	
	public DefaultCaseFilterChain(Iterator<CaseFilter> filterIter) {
		this.filterIter = filterIter;
	}
	
	@Override
	public void doFilter(Case c) {
		try {
			if (filterIter.hasNext()) {
				filterIter.next().filter(c, this);
			}
		}
		catch(Throwable t) {
			c.result().addError(t.getMessage(), t);
		}
	}
}
