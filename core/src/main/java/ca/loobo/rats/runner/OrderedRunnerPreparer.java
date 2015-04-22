package ca.loobo.rats.runner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.CaseSessionHolder;
import ca.loobo.rats.caze.TestContext;
import ca.loobo.rats.exceptions.CaseNotFoundException;

/**
 * A case depends on the others in 3 ways
 * 		1. declare in meta columns of Excel resource file 
 *         with title "dependencies" and value caseId1, caseId2
 *      2. refer to the other cases in parameter list
 *      3. refer to the other cases in expected response value list
 *      
 * Cases should be ordered while they are composed, referred class should appear prior to dependent case
 * This class was used to automatically order disordered cases, but consider that will be it will be hard
 * to maintain that case, so removed the feature.  Disordered case will lead to exceptions while case is running
 * @author robertx
 *
 */
@Component
public class OrderedRunnerPreparer {
	private final Logger logger = LoggerFactory.getLogger(OrderedRunnerPreparer.class);
	
	@Autowired
	CaseRunnerFactory caseRunnerFactory;
	
	private LinkedHashMap <String, CaseRunner> runners = new LinkedHashMap <String, CaseRunner>();
	private TestContext context = new TestContext();
	
	public TestContext getContext() {
		return this.context;
	}
	
	public void prepare() {
		CaseSessionHolder.setContext(context);
		LinkedList<Case> pendings = new LinkedList<Case>(context.getCases());
		
		int i = 0;
		while(pendings.size()>0) {
			Case caze = pendings.pop();
			try {
				CaseSessionHolder.currentSession().setCase(caze);
				CaseRunner runner = caseRunnerFactory.newCaseRunner(context, caze);
				logger.debug("{} adding runner for {}", ++i, caze.getId());
				this.runners.put(caze.getId(), runner);
			} catch(CaseNotFoundException e) {
				//throw it out if it is not in the original case set
				if (caze.getContext().getCase(e.getCaseId()) == null) {
					throw new CaseNotFoundException(e.getCaseId());
				}
				
				pendings.addLast(caze);
			}
		}		
	}
	
	public List<Runner> getRunners() {
		return new ArrayList<Runner>(runners.values());
	}

}
