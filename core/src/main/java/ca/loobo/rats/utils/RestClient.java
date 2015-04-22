package ca.loobo.rats.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ca.loobo.rats.caze.TestContext;


public class RestClient {
	private int timeout = 30 * 1000;	//30 seconds by default
	private String mHost = null;
	private HttpHeaders requestHeaders = new HttpHeaders();
	private BeforeRequestStartListener listener;
	
	public RestClient(TestContext ctx) {
		for(String key : ctx.getParamMap().keySet()) {
			if (key.startsWith("header-")) {
				String value = ctx.getParam(key);
				addRequestHeader(key.substring(7), value);
			}
		}
	}
	
	public RestClient(int timeout) {
		this.timeout = timeout;
	}
	
	public String getForString(String url) {
			return exchangeForString(url, null, HttpMethod.GET);
	}
	
	public String getForString(String templateUrl, Map<String, ?> vars)  {
	  return exchange(templateUrl, null, HttpMethod.GET, vars).getBody();
	}

	public ResponseEntity<String> head(String url, Map<String, ?> vars) {
		return exchange(url, null, HttpMethod.HEAD, vars);
	}
	
	public ResponseEntity<String> get(String url, Map<String, ?> vars) {
		return exchange(url, null, HttpMethod.GET, vars);
	}
	
	public ResponseEntity<String> delete(String url, Map<String, ?> vars) {
		return exchange(url, null, HttpMethod.DELETE, vars);
	}
	
	public ResponseEntity<String> put(String url, String data, Map<String, ?> vars) {
		return exchange(url, data, HttpMethod.PUT, vars);
	}
	
	public ResponseEntity<String> post(String url, String data, Map<String, ?> vars) {
		return exchange(url, data, HttpMethod.POST, vars);
	}
	
	private String exchangeForString(String url, String data, HttpMethod method) {
	  return exchange(url, data, method, new HashMap<String, String>()).getBody();
	}

	/**
	 * 
	 * @param urlTemplate		http://localhost:8080/path?var1={value1}&var2={value2}
	 * @param data		request body
	 * @param method	request method
	 * @param vars		used for replacing variables in URL template, ex: map.put("var1", "value1"}
	 * @return
	 */
	private ResponseEntity<String> exchange(String urlTemplate, String data, HttpMethod method, Map<String, ?> vars)  {

			if (this.listener != null) {
				this.listener.onRequest(this);
			}
		
			requestHeaders.set("Connection",  "Close");
			if (mHost != null) {
				requestHeaders.set("Host", mHost);
			}

			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
			HttpEntity<?> requestEntity = null;
			if (data == null) {
				requestEntity = new HttpEntity<Object>(requestHeaders);
			} else {
				requestHeaders.setContentType(MediaType.APPLICATION_JSON);
				requestEntity = new HttpEntity<String>(data, requestHeaders);
			}
	
			RestTemplate restTemplate = newRestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.exchange(
					urlTemplate, method, requestEntity, String.class, 
					vars == null ? new HashMap<String, Object>() : vars
					);
			
			
			return responseEntity;

	}
	
	public void addRequestHeader(String name, String value) {
		requestHeaders.add(name, value);
	}
	
	public RestClient setHost(String host) {
		mHost = host;
		return this;
	}
	
	private RestTemplate newRestTemplate () {
		RestTemplate restTemplate = new RestTemplate();
		ClientHttpRequestFactory rf = restTemplate.getRequestFactory();
        if (rf instanceof SimpleClientHttpRequestFactory) {
            ((SimpleClientHttpRequestFactory) rf).setConnectTimeout(timeout);
            ((SimpleClientHttpRequestFactory) rf).setReadTimeout(timeout);
        } else if (rf instanceof HttpComponentsClientHttpRequestFactory) {
            ((HttpComponentsClientHttpRequestFactory) rf).setReadTimeout(timeout);
            ((HttpComponentsClientHttpRequestFactory) rf).setConnectTimeout(timeout);
        }        
        
		//remove all default message converters, only use the one defined below for removing AcceptCharset header which is huge
		restTemplate.getMessageConverters().clear();
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setWriteAcceptCharset(false);
		restTemplate.getMessageConverters().add(converter);		
        
        return restTemplate;
	}

	public void setBeforeRequestStartListener(BeforeRequestStartListener l) {
		this.listener = l;
	}
}
