package com.quickplay.restbot.param;

import java.net.URI;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quickplay.restbot.caze.TestContext;
import com.quickplay.restbot.param.resolver.DefaultValueResolverChain;
import com.quickplay.restbot.param.resolver.ValueResolverChain;

public class DBValueParamProducer implements ParamProducer {
	final static Logger logger = LoggerFactory.getLogger(DBValueParamProducer.class);
	
	@Override
	public String value(TestContext context, String value) {
		
		URI valueUrl = URI.create(value);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ")
		.append(valueUrl.getPath().substring(1))
		.append(" FROM ")
		.append(valueUrl.getAuthority())
		.append(" WHERE ");
		String[] params = valueUrl.getQuery().split("&");
		LinkedList<String> pairs = new LinkedList<String>();
		
		ValueResolverChain chain = new DefaultValueResolverChain();
		for(String param : params) {
			String[] pair = param.split("=",2);
			pairs.add(pair[0] +"='" + chain.resolve(pair[1]) + "'");
		}
		sql.append(StringUtils.join(pairs, " AND "));
		logger.debug(sql.toString());
		return context.getJdbcTemplate().queryForObject(sql.toString(), String.class);
	}

}
