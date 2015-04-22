package com.quickplay.restbot.utils;

public interface BeforeRequestStartListener {

	void onRequest(RestClient client);
}
