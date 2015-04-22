package ca.loobo.rats.utils;

public interface BeforeRequestStartListener {

	void onRequest(RestClient client);
}
