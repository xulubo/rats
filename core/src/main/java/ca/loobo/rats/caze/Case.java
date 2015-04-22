package ca.loobo.rats.caze;

import static ca.loobo.rats.caze.MetaName.postscript;
import static ca.loobo.rats.caze.MetaName.prescript;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.loobo.rats.param.CollectionParamValue;
import ca.loobo.rats.param.DefaultParamValue;
import ca.loobo.rats.utils.ParamUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Case extends AbstractCase {
	final static Logger logger = LoggerFactory.getLogger(Case.class);
	
	@JsonIgnore
	List<ChildCase> childCases;
	
	private Map<String, Object> attributes = Maps.newHashMap();
	private String requestBody;
	
	public Case(TestContext ctx) {
		super(ctx);
	}
	
	public List<ChildCase> childs() {
		if (childCases != null) {
			return childCases;
		}
		
		boolean isCollectionCase = false;
		List<Map<String, ParamValue>> paramList = Lists.newLinkedList();

		for (Entry<String, ParamValue> pair : this.queryParams.entrySet()) {
			String name = pair.getKey();
			ParamValue expression = pair.getValue();
			Collection<String> paramValues;// = RangeValueParamProducer.value(pair.getValue().toString());
			if (expression instanceof CollectionParamValue) {
				isCollectionCase = true;
				paramValues = ((CollectionParamValue)expression).values();
			}
			else {
				//don't need to parse value here, it will be parsed as case is being executed
				paramValues = Arrays.asList(pair.getValue().value());
			}

			List<Map<String, ParamValue>> newList = Lists.newLinkedList();

			for (String value : paramValues) {
				if (paramList.size() == 0) {
					Map<String, ParamValue> paramMap = Maps.newHashMap();
					paramMap.put(name, new DefaultParamValue(value));
					newList.add(paramMap);
					continue;
				}
				for (Map<String, ParamValue> paramMap : paramList) {
					Map<String, ParamValue> newMap = new HashMap<String, ParamValue>(paramMap);
					newMap.put(name, new DefaultParamValue(value));
					newList.add(newMap);
				}
			}

			paramList = newList;

		}

		childCases = new LinkedList<ChildCase>();
		if (isCollectionCase) {
			int i=0;
			for(Map<String, ParamValue> params : paramList) {
				ChildCase cc = new ChildCase(getContext(), this, params, i++);
				childCases.add(cc);
			}
		}
		
		return childCases;
	}

	public Map<String, String> getMetas() {
		return this.metas;
	}

	public Map<String, ParamValue> getExpectations() {
		return this.expectations;
	}

		
	public Map<String, String> getPathVariables() {
		return Collections.unmodifiableMap(this.pathVariables);
	}
	
	public void addQueryParam(String name, String value) {
		if (CollectionParamValue.matches(value)) {
			this.queryParams.put(name, new CollectionParamValue(value));			
		}
		else {
			this.queryParams.put(name, new DefaultParamValue(value));
		}
	}
	
	public void addPathVariable(String name, String value) {
		this.pathVariables.put(name, value);
	}

	public String getPreScript() {
		String str = this.getMetas().get(prescript.name());
		return ParamUtils.extend(str,this.getAllVaribles());
	}	
	
	public String getPostScript() {
		String str = this.getMetas().get(postscript.name());
		return ParamUtils.extend(str,this.getAllVaribles());
	}

	public String getRequestBody() {
		if (requestBody == null) {
			String str = this.getMeta(MetaName.body);
			requestBody = ParamUtils.extend(str, this.getAllVaribles());
		}
		return requestBody;
	}
	
	public void setRequestBody(String body) {
		requestBody = body;
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

}
