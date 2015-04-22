package com.quickplay.restbot.caze;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.collect.Maps;
import com.quickplay.restbot.AppContext;
import com.quickplay.restbot.exceptions.CaseNotFoundException;
import com.quickplay.restbot.exceptions.ResourceNotFoundException;
import com.quickplay.restbot.validators.DefaultValueReaderChain;
import com.quickplay.restbot.validators.ValueReader;

/**
 * Shared by a suite of tests (an single excel file define a test suite)
 * Cases can be related, so a case can reference variable from another case within the same context
 * @author robertx
 *
 */
@Component
public class TestContext {
	static final Logger logger = LoggerFactory.getLogger(TestContext.class);

	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String SERVICE_PATH = "servicePath";
	public static final String FIXED_PARAMS = "fixedParams";
	public static final String RESOURCE_FOLDER = "resourceFolder";
	public static final String DB_URL="dbUrl";
	public static final String DB_USERNAME="dbUsername";
	public static final String DB_PASSWORD="dbPassword";

	//use LinkedHashMap to keep the elements order the same as they are inserted
	private LinkedHashMap <String, Case> cases = new LinkedHashMap <String, Case>();
	private String host;
	private Integer port;
	private TestReport result;
	private String outDir = "output";
	private String resDir = "resources";
	private Map<String, String> params = Maps.newConcurrentMap();
	private List<Pattern> allowFilters;
	private Map<String, TestSuite> suites = Maps.newConcurrentMap();
	private JdbcTemplate jdbcTemplate;
	private String instanceOutputDir;
	
	public TestContext() {
		result = new TestReport(this);
		allowFilters = new LinkedList<Pattern>();
	}
	
	public void initialize() {
		Assert.notNull(getHost(), "host is not defined");
	}
	
	public String getHost() {
		return host == null ? getParam(HOST) : host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port == null ? getParam(PORT) : port.toString();
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Collection<Case> getCases() {
		return this.cases.values();
	}
	
	private boolean isAllowed(String caseId) {
		if (allowFilters.size()==0) {
			return true;
		}
		
		for(Pattern m : allowFilters) {
			if (m.matcher(caseId).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public void addAllowFilter(String filter) {
		this.allowFilters.add(Pattern.compile(filter));
	}
	
	//add case to the global case pool and suite pool
	public void addCase(String caseId, Case apicase, String suiteName) {
		if (isAllowed(caseId)) {
			this.cases.put(caseId, apicase);
			this.getSuite(suiteName).addCase(apicase);
		}
	}
	
	public TestSuite getSuite(String suiteName) {
		TestSuite suite = suites.get(suiteName);
		if (suite == null) {
			suite = new TestSuite(suiteName);
			suites.put(suiteName, suite);
		}
		
		return suite;
	}
	
	public Collection<String> getSuiteNames() {
		return this.suites.keySet();
	}
	
	public Case getCase(String caseId) {
		Case caze = this.cases.get(caseId);
		if (caze == null) {
			throw new CaseNotFoundException(caseId);
		}
		
		return caze;
	}
	
	public void addFinishedCase(Case c) {
		this.result.add(c);
	}

	public TestReport getResult() {
		return this.result;
	}
	

	public Resource getResource(String resourcePath) {
		Resource resource;

		String path = getParam(RESOURCE_FOLDER);
		if (StringUtils.isBlank(path)) {
			path = System.getProperty("user.dir");
		}
		
		path += "/" + resourcePath;
		resource = AppContext.instance().getApplicationContext().getResource(path);
		if (resource != null && resource.isReadable()) {
			return resource;
		}		

		// find resource in Windows-style absolute path
		else if (path.matches("[a-zA-Z]+:.*")) {
			logger.trace("not found {}", path);
			throw new ResourceNotFoundException("Couldn't read resource file " + path);
		}
		// find resource in path relative to the current folder
		else {
			path = "file:" + path;
		}
		
		logger.debug("reading {}", path);
		resource = AppContext.instance().getApplicationContext().getResource(path);
		
		if (resource == null || !resource.isReadable()) {
			throw new ResourceNotFoundException("Couldn't read resource file " + path);
		}

		return resource;
	}
	
	public String getOutputDir() {
		if (instanceOutputDir != null) {
			return instanceOutputDir;
		}
		
		File dir = new File(outDir);
		if (!dir.isDirectory()) {
			Assert.isTrue(dir.mkdir(), "make output folder");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		
		instanceOutputDir = outDir + File.separator + sdf.format(new Date());
		new File(instanceOutputDir).mkdirs();
		
		return instanceOutputDir;
	}
	/**
	 * Set output directory, all output files will be saved in this folder
	 * 
	 * @param outDir
	 * @return
	 */
	public void setOutputDir(String outDir) {
		this.outDir = outDir;
		getOutputDir();
	}
	
	public String getResourceDir() {
		return resDir;
	}
	public void setResourceDir(String resDir) {
		this.resDir = resDir;
	}

	public Map<String, String> getParamMap() {
		return this.params;
	}
	
	/*
	 * System property always overwrite parameter defined in Excel test schema
	 * Applied parameters includes host, port, resourceFolder
	 */
	public String getParam(String paramName) {
		return System.getProperty(paramName, params.get(paramName));
	}


	public UriComponentsBuilder getUriBuilder() {
		String p;
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme("http");
		builder.host(getHost());
		if (StringUtils.isNotBlank(p=getPort())) {
			builder.port(Integer.valueOf(p.trim()));
		}
		
		if (StringUtils.isNotBlank(p=params.get(SERVICE_PATH))) {
			builder.path(p.trim());
		}
		
		if (StringUtils.isNotBlank(p=params.get(FIXED_PARAMS))) {
			builder.query(p.trim());
		}
		

		return builder;
	}

	public void addVariable(String name, String value) {
		String varValue = this.params.get(name);
		if (varValue != null && !varValue.equals(value)) {
			throw new RuntimeException("variable " + name + " has been defined already");
		}
		this.params.put(name, value);
	}
	
	public File getOutputFile(String basename) {
		basename = basename.replaceAll("[\\\\/:*?\"<>|]", "_");
		return new File(getOutputDir() + "/" + basename);
	}	
	
	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			String url = getParam(TestContext.DB_URL);
			String username = getParam(TestContext.DB_USERNAME);
			String password = getParam(TestContext.DB_PASSWORD);
			DataSource dataSource = new SimpleDriverDataSource(new oracle.jdbc.driver.OracleDriver(), url, username, password);
			jdbcTemplate = new JdbcTemplate(dataSource);			
		}
		
		return jdbcTemplate;
	}
	
	public Object readCaseValue(Case caze, String valName) {

		return new DefaultValueReaderChain().read(caze, valName);
	}
}
