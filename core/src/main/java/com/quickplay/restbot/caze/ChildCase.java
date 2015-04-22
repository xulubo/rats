package com.quickplay.restbot.caze;

import static com.quickplay.restbot.caze.MetaName.id;

import java.util.Map;
import java.util.Map.Entry;

import com.quickplay.restbot.param.DefaultParamValue;

public class ChildCase extends Case {

	final Case parent;
	final int seqNo;

	public ChildCase(TestContext ctx, Case parent, Map<String, ParamValue> queryParams, int seqNo) {
		super(ctx);
		this.parent = parent;
		this.seqNo = seqNo;
		this.queryParams = queryParams;

		this.metas = parent.getMetas();
		this.pathVariables.putAll(parent.getPathVariables());
		
		for(Entry<String, ParamValue> entry : parent.getExpectations().entrySet()) {
			this.expectations.put(entry.getKey(), new DefaultParamValue(entry.getValue().value()));
		}
	}

	public int getSeqNo() {
		return seqNo;
	}
	
	@Override
	public String getId() {
		return super.getId() + "%" + getSeqNo();
	}
}
