package com.quickplay.restbot.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.jayway.jsonpath.JsonPath;
import com.quickplay.restbot.exceptions.ParameterNotFoundException;

public class ParamUtils {
	private static Logger logger = LoggerFactory.getLogger(ParamUtils.class);
	private static final Pattern VAR_PATTERN = Pattern
			.compile("\\$?\\{([^/]+?)\\}");

	/*
	 * @Param defaultParamValue replacement of missing parameter,
	 * IllegalParameterException will be thrown if defaultStr is null and there
	 * is missing parameter
	 */
	public static Collection<String> extractVariableNames(String template) 
	{
		Set<String> names = Sets.newHashSet();
		if (TextUtils.isEmpty(template)) {
			return names;
		}

		Matcher m = VAR_PATTERN.matcher(template);

		while (m.find()) {
			String match = m.group(1);
			names.add(match);
		}

		return names;
	}
	
	/*
	 * @Param defaultParamValue replacement of missing parameter,
	 * IllegalParameterException will be thrown if defaultStr is null and there
	 * is missing parameter
	 */
	private static String doExtend(String template, Map<String, String> params,
			String defaultParamValue) 
	{
		//Assert.hasText(template, "'template' must not be null");
		if (TextUtils.isEmpty(template)) {
			return "";
		}

		Matcher m = VAR_PATTERN.matcher(template);
		StringBuilder sb = new StringBuilder();

		int end = 0;
		while (m.find()) {
			sb.append(template.substring(end, m.start()));
			String match = m.group(1);

			//
			if (params != null && match.startsWith("$")) {

				String json = params.get("$");
				if (json != null) {
					logger.debug("{}", json);
					try {
						if (match.equals("$")) {
							sb.append(json);
						}
						else {
							String content = JsonPath.read(json, match);
							if (content != null) {
								sb.append(content);
							}
							else {
								logger.debug("no content was found by json path {}", match);
							}
						}
					}
					catch(Exception e) {
						//ignore
					}
				}
			}
			
			else if (params != null && params.containsKey(match)) {
				sb.append(params.get(match));
			} else if (defaultParamValue != null) {
				sb.append(defaultParamValue);
			} else {
				logger.error("couldn't extend string {}", template);
				throw new ParameterNotFoundException(match);
			}

			end = m.end();
		}

		if (end > 0) {
			sb.append(template.substring(end));
		}
		else {
			return template;
		}

		return sb.toString();
	}

	public static boolean isExtendable(String input) {
			Matcher m = VAR_PATTERN.matcher(input);
			return m.find();
	}
			
	public static String extend(String template, Map<String, String> params,
			String defaultParamValue) {
		return doExtend(template, params, defaultParamValue);
	}

	public static String extend(String template, Map<String, String> params) {
		
		String val = doExtend(template, params, null);
		//if (isExtendable(val)) {
		//	return extend(val, params, null);
		//}
		
		return val;
	}

	public static boolean matchesIgnoreCase(String pattern, String input, Map<String, String> values) {

		Matcher m = Pattern.compile(pattern.trim().replaceAll(" +", " ").toLowerCase()).matcher(input.trim().toLowerCase());
		
		if (!m.matches()) {
			return false;
		}
		
		if (values != null) {
			for(int i=1; i<=m.groupCount();i++) {
				logger.debug("start {} end {}", m.start(i), m.end(i));
				values.put(String.valueOf(i), m.group(i));
			}
		}

		return true;
	}
}
