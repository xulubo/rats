package com.quickplay.restbot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class SingleTest {

	@Test
	public void test() {

		check(new Integer(1).longValue());
	}
	

	void check(Long i) {
		System.err.println(i);
	}
}
