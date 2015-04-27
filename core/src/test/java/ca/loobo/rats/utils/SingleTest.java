package ca.loobo.rats.utils;

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
