package com.quickplay.restbot.caze;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.quickplay.restbot.AppContext;

public class TestReport {
	final static Logger logger = LoggerFactory.getLogger(TestReport.class);
	
	public ArrayList<Case> all = new ArrayList<Case>();
	public List<Case> blocked = new LinkedList<Case>();
	private TestContext context;
	
	public TestReport(TestContext context) {
		this.context = context;
	}
	
	public void add(Case c) {
		writeResponseToFile(c);
		all.add(c);
	}

	public ArrayList<Case> getAllCases() {
		return this.all;
	}
	
	public void log(FileWriter logWriter, Case a) throws IOException {
		logWriter.append(a.getId());
		logWriter.append("> ");
		logWriter.append(a.result().isPassed() ? "PASSED: " : "FAILED: ");
		logWriter.append(a.buildUrl());
		logWriter.append("\n");
		for (String info : a.result().getErrors()) {
			logWriter.append(info);
			logWriter.append("\n");
		}
		logWriter.append(a.getResponseBody());
		logWriter.append("\n\n");
		logWriter.append("---------------------------------\n");
	}
	
	public void dump() throws IOException {
		StringBuilder dumpMsg = new StringBuilder();
		int failed = 0, passed = 0;
		for(Case c : all) {
			if (c.result().isPassed()) {
				passed++;
			}
			else {
				dumpMsg.append(String.format("-- FAILED TEST %s ----------\n", c.getId()));
				for(String s : c.result().getErrors()) {
					dumpMsg.append("\t").append(s).append("\n");
				}
				dumpMsg.append("\n");
			}
		}
		
		dumpMsg.append(String.format("TOTAL %d PASSED %d FAILED %d BLOCKED %d   ", 
				this.context.getCases().size(), passed, failed, blocked.size()));
		
		logger.info(dumpMsg.toString());

	
		try {
			makeReport();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	private CaseReport createCaseReport(Case c) {
		CaseReport r = new CaseReport();
		
		if (c instanceof ChildCase) {
			ChildCase cc = (ChildCase)c;
			r.setId(cc.getId());
		}
		else {
			r.setId(c.getId());			
		}
		r.setDescription(c.getMeta(MetaName.description));
		r.setResult(c.result().isPassed() ? "passed" : "failed");
		try {
		r.setUrl(c.buildUrl());
		} catch(Exception e) {
			System.err.print("Case ID: " + c.getId());
		}
		
		StringBuilder sb = new StringBuilder();
		for(String error : c.result().getErrors()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(error);
		}
		r.setErrors(sb.toString());

		return r;
	}
	
	public void makeReport() {
		List<CaseReport> caseResults = new LinkedList<CaseReport>();
		for(Case c : all) {
			if (c.isCollectionCase()) {
				for(ChildCase cc : c.childs()) {
					caseResults.add(createCaseReport(cc));
				}
			}
			else {
				caseResults.add(createCaseReport(c));
			}
		}
		
		try {
			InputStream is = AppContext.getResource("classpath:report.template").getInputStream();
			String reportTemplate = IOUtils.toString(is);
			String data = new Gson().toJson(caseResults);
			String report = reportTemplate.replace("{TEST_DATA}", data);
			FileWriter reportFile = new FileWriter(context.getOutputFile("result.html"));
			reportFile.append(report);
			reportFile.close();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void writeResponseToFile(Case c) {
		if (c.isCollectionCase()) {
			for(ChildCase cc : c.childs()) {
				writeResponseToFile(cc.getId(), cc.getResponseBody());					
			}
		}
		else {
			writeResponseToFile(c.getId(), c.getResponseBody());	
		}
	}
	
	private void writeResponseToFile(String id, String content) {
		File f = context.getOutputFile("response_" + id+".json");
		try {
			if (content != null) {
				FileWriter w = new FileWriter(f);
				w.write(content);
				w.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
