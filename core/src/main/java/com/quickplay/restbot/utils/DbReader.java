package com.quickplay.restbot.utils;

import java.net.URI;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.quickplay.restbot.param.DBValueParamProducer;

public class DbReader {

	final static Logger logger = LoggerFactory.getLogger(DBValueParamProducer.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public DbReader(String url, String username, String password) {
		dataSource = new SimpleDriverDataSource(new oracle.jdbc.driver.OracleDriver(), url, username, password);
		jdbcTemplate = new JdbcTemplate(dataSource);			
	}

	public String singleValue(URI valueUrl) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ")
		.append(valueUrl.getPath().substring(1))
		.append(" FROM ")
		.append(valueUrl.getAuthority())
		.append(" WHERE ");
		String[] params = valueUrl.getQuery().split("&");
		LinkedList<String> pairs = new LinkedList<String>();
		for(String param : params) {
			String[] pair = param.split("=",2);
			pairs.add(pair[0] +"='" + pair[1] + "'");
		}
		sql.append(StringUtils.join(pairs, " AND "));
		logger.debug(sql.toString());
		return jdbcTemplate.queryForObject(sql.toString(), String.class);
	}
	
}
