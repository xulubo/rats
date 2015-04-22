package ca.loobo.rats.runner;

import java.util.List;

import org.apache.log4j.NDC;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.ChildCase;
import ca.loobo.rats.caze.MetaName;
import ca.loobo.rats.caze.TestContext;
import ca.loobo.rats.exceptions.PriorCaseNotExecutedException;
import ca.loobo.rats.filter.CaseFilter;
import ca.loobo.rats.filter.DefaultCaseFilterChain;

public class CollectionCaseRunner extends CaseRunner {
	
	private static Logger logger = LoggerFactory.getLogger(CollectionCaseRunner.class);
	static int index = 0;
	
	final Description description;
	final TestContext context;
	
	public CollectionCaseRunner(TestContext ctx, Case c, List<CaseFilter> caseFilters) {
		super(c, caseFilters);
		this.context = ctx;
		description = Description.createTestDescription("Collection Test", c.getId().toString());
	}
	
	@Override
	public Description getDescription() {
		return this.description;
	}

	@Override
	public void run(RunNotifier notifier) {
		Assert.notNull(notifier);
		
		try {
			logger.debug("--- BEGIN Test {} ---", mCase.getId());
			NDC.push("\t");
			notifier.fireTestStarted(getDescription());
			
			for(ChildCase cc : mCase.childs()) {
				new DefaultCaseFilterChain(getFilters().iterator()).doFilter(cc);
				if (cc.result().getErrors().size() > 0) {
					mCase.result().addError("failed: " + cc.buildUrl(), new Throwable());
					for(String err : cc.result().getErrors()) {
						mCase.result().addError(err, new Throwable());
					}
				}
			}
			
			if (mCase.result().getErrors().size()>0) {
				StringBuilder sb = new StringBuilder();
				sb.append("Case Description: ");
				sb.append(mCase.getMeta(MetaName.description)).append("\n");
				for(String info : mCase.result().getInfos()) {
					sb.append("    ").append(info).append("\n");
				}
				sb.append("Errors: \n");
				for(String err : mCase.result().getErrors()) {
					sb.append("    ").append(err).append("\n");
				}
				notifier.fireTestFailure(new Failure(getDescription(), new AssertionError(sb.toString())));
			}			
			this.context.addFinishedCase(mCase);
		} catch(PriorCaseNotExecutedException e) {
			notifier.fireTestFailure(new Failure(getDescription(), new AssumptionViolatedException(e.toString())));
		}
		
		finally {
			notifier.fireTestFinished(getDescription());
			NDC.clear();
			logger.debug("--- END Test {} ---\n\n", mCase.getId());
		}

	}

}
