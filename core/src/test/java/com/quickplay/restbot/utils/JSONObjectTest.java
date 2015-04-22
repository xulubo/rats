package com.quickplay.restbot.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JSONObjectTest {

	@Test
	public void test() throws IOException, JSONException {
		byte[] b = Files.readAllBytes(new File("c:/tmp/blackout_response.json").toPath());
		
		String s = new String(b);
		
		Object p = new JSONObject(s).getJSONArray("schedules").get(0);
		JSONObject o = new JSONObject(s);
		
		o.put("blackout", p);
		
		System.err.println(o.toString());
	}
}
