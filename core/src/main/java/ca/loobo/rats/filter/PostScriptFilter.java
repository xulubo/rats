package ca.loobo.rats.filter;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.expr.ExprEvaluator;

@Component
public class PostScriptFilter extends ScriptFilter {

	final static Logger logger = LoggerFactory.getLogger(PostScriptFilter.class);
	
	@Resource
	ExprEvaluator exprEvaluator;
	
	@Override
	public String name() {
		return "POST_SCRIPT";
	}
	
	@Override
	public void filter(Case c, CaseFilterChain chain) {
		execute(c.getPostScript());
		chain.doFilter(c);
	}
}
