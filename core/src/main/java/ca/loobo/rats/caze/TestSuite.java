package ca.loobo.rats.caze;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestSuite {

	private final Map<String, Case> cases = Maps.newLinkedHashMap();
	private final List<String> metaHeads = Lists.newLinkedList();
	private final List<String> parameterHeads = Lists.newLinkedList();
	private final List<String> responseHeads = Lists.newLinkedList();
	
	public TestSuite(String name) {
		
	}
	
	public void addCase(Case caze) {
		this.cases.put(caze.getId(), caze);
	}
	
	public void addMetaHead(String headName) {
		metaHeads.add(headName);
	}
	
	public void addParameterHead(String headName) {
		parameterHeads.add(headName);
	}
	
	public void addResponseHead(String headName) {
		responseHeads.add(headName);
	}
	
	public Map<String, Case> getTestCases() {
		return Collections.unmodifiableMap(cases);
	}
	
	public List<String> getMetaHeads() {
		return this.metaHeads;
	}
	public List<String> getParameterHeads() {
		return this.parameterHeads;
	}
	public List<String> getResponseHeads() {
		return this.responseHeads;
	}	
}
