package ca.loobo.rats.filter;

import org.springframework.stereotype.Component;

import ca.loobo.rats.caze.Case;

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
