package com.quickplay.restbot.runner;

import java.util.List;

import org.apache.log4j.NDC;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.caze.MetaName;
import com.quickplay.restbot.caze.TestContext;
import com.quickplay.restbot.exceptions.PriorCaseNotExecutedException;
import com.quickplay.restbot.filter.CaseFilter;
import com.quickplay.restbot.filter.DefaultCaseFilterChain;

public class SingleCaseRunner extends CaseRunner {
	
	private static Logger logger = LoggerFactory.getLogger(SingleCaseRunner.class);
	static int index = 0;
	
	final Description description;
	final TestContext context;
	
	public SingleCaseRunner(TestContext ctx, Case c, List<CaseFilter> caseFilters) {
		super(c, caseFilters);
		this.context = ctx;
		description = Description.createTestDescription("Test", c.getId().toString());
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
			new DefaultCaseFilterChain(getFilters().iterator()).doFilter(mCase);

			this.context.addFinishedCase(mCase);
			
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
