package com.quickplay.restbot.runner;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.quickplay.restbot.AppContext;
import com.quickplay.restbot.annotations.Config;
import com.quickplay.restbot.annotations.Filters;
import com.quickplay.restbot.annotations.Host;
import com.quickplay.restbot.annotations.ResourceFiles;
import com.quickplay.restbot.caze.TestContext;
import com.quickplay.restbot.exceptions.CaseNotExecutedException;
import com.quickplay.restbot.reader.ResourceReader;

public abstract class AbstractSuiteRunner extends ParentRunner<Runner> implements RunnerScheduler{
	
	@Autowired
	private OrderedRunnerPreparer preparer;

	@Autowired
	private CaseRunnerFactory caseRunnerFactory;
	
	private static Logger logger = LoggerFactory.getLogger(SuiteRunner.class);
	private LinkedList<Resource> resources = new LinkedList<Resource>();
	private LinkedList<Runner> pendingQueue = new LinkedList<Runner>();
	private RunNotifier notifier = new RunNotifier();
	final private ResourceReader resourceReader;

	public AbstractSuiteRunner(Class<?> testClass, ResourceReader resReader) throws InitializationError {
		super(testClass);
		Config cfg = testClass.getAnnotation(Config.class);
		
		AppContext.addConfig(cfg);
		AppContext.autowireBean(this);
		
		caseRunnerFactory.inject(testClass.getAnnotation(Filters.class));
		
		resourceReader = resReader;
		Host h = testClass.getAnnotation(Host.class);
		if (h != null) {
			getContext().setHost(h.host());
			getContext().setPort(h.port());
		}

		ResourceFiles r = testClass.getAnnotation(ResourceFiles.class);
		if (r == null || r.value().length==0) {
			//throw new InitializationError("no resource file is defined");
		}
		else {	
			for(String p : r.allows()) {
				this.getContext().addAllowFilter(p);
			}
			for(String f : r.value()) {
				this.addResource(f);
			}
		}

		prepare();
	}

	public void prepare() {
		this.setScheduler(this);
		preparer.prepare();
		logger.debug("{}: {}", TestContext.RESOURCE_FOLDER, getContext().getParam(TestContext.RESOURCE_FOLDER));
		logger.debug("{}: {}", TestContext.HOST, getContext().getHost());
		logger.debug("{}: {}", TestContext.PORT, getContext().getPort());
		
		for(Resource r : resources) {
			logger.debug("ResourceFile: {}", r.getFilename());
		}
	}
	
	public void start() {
		try {
			prepare();
			launch();
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public TestContext getContext() {
		return this.preparer.getContext();
	}
	
	public void launch() throws IOException {
		logger.debug("reading excel ...");

		for (Runner runner : this.preparer.getRunners()) {
			try {
				runner.run(notifier);
			}
			catch (CaseNotExecutedException e) {
				pendingQueue.add(runner);
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
		}

		//just use a simple way to avoid dead loop in the worst case
		//not elegant way, but easy and enough for now
		int pendingCaseCounter = 0;
		while(pendingQueue.size()>0 && pendingCaseCounter<999) {
			Runner runner = pendingQueue.pop();
			try {
				pendingCaseCounter++;
				runner.run(null);
			}
			catch (CaseNotExecutedException e) {
				pendingQueue.add(runner);
			}				
		}

	}
	
	private void parseResource(Resource resFile) throws InitializationError {
		this.resourceReader.read(this.getContext(), resFile);
	}
	
	public AbstractSuiteRunner addFileResource(String resPath) throws InitializationError {
		Resource res = new FileSystemResource(resPath);
		parseResource(res);
		this.resources.add(res);
		return this;
	}

	public AbstractSuiteRunner addResource(String resPath) throws InitializationError {
		Resource res = AppContext.instance().getApplicationContext().getResource(resPath);
		parseResource(res);
		this.resources.add(res);
		return this;
	}

	@Override
	protected List<Runner> getChildren() {
		return this.preparer.getRunners();
	}

	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	@Override
	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}
    
	@Override
    public void schedule(Runnable childStatement) {
        childStatement.run();
    }

	@Override
    public void finished() {
		try {
			this.getContext().getResult().dump();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
