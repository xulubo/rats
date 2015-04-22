package com.quickplay.restbot.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.caze.CaseSessionHolder;
import com.quickplay.restbot.caze.ChildCase;
import com.quickplay.restbot.exceptions.CaseExecutionException;
import com.quickplay.restbot.utils.BeforeRequestStartListener;
import com.quickplay.restbot.utils.RestClient;

public class CaseExecutionFilter implements CaseFilter {
	static Logger logger = LoggerFactory.getLogger(CaseExecutionFilter.class);

	@Override
	public String name() {
		return "CASE_EXECUTION";
	}
	
	@Override
	public void filter(Case c, CaseFilterChain chain) {
		logger.debug("Enter");
		try {
			execute(c);
		}
		catch(Throwable t) {
			throw new CaseExecutionException(t.getMessage(), t);
		}
		chain.doFilter(c);
	}

	
	private boolean execute(Case c) {
		try {
			if (c.isCollectionCase()) {
				logger.debug("handling group case: " + c.getId());
				List<ChildCase> cases = c.childs();
				for(ChildCase cc : cases) {
					logger.debug("requesting {} ", cc.buildUrl());
					c.result().addInfo("url: " + cc.buildUrl());
					request(cc);
				}
				c.setResponseEntity(null);
			}
			else {
				request(c);
			}

		} catch (HttpClientErrorException e) {
			MediaType mediaType = e.getResponseHeaders().getContentType();
			if (!mediaType.includes(MediaType.APPLICATION_JSON)) {
				throw new CaseExecutionException(e.getMessage(), e);		
			}
			
			c.setResponseEntity(new ResponseEntity<String>(e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode()));
		} catch (Throwable t) {
			throw new CaseExecutionException(t.getMessage(), t);		
		}

		c.result().setExecuted();
		return true;
	}
	
	private boolean request(Case c) {
		RestClient restClient = new RestClient(c.getContext());
		BeforeRequestStartListener l = (BeforeRequestStartListener)CaseSessionHolder.currentSession().getAttribute(BeforeRequestStartListener.class.getName());
		restClient.setBeforeRequestStartListener(l);
		logger.debug("requesting {}", c.buildUrl());
		c.result().addInfo("url: " + c.buildUrl());

		ResponseEntity<String> responseEntity = null;
		switch(c.getRequestMethod()) {
		case GET:
			responseEntity = restClient.get(c.buildUrl(), c.getPathVariables());
			break;
		case DELETE:
			responseEntity = restClient.delete(c.buildUrl(), c.getPathVariables());
			break;
		case PUT:
			responseEntity = restClient.put(c.buildUrl(), c.getRequestBody(), c.getPathVariables());
			break;
		case POST:
			responseEntity = restClient.post(c.buildUrl(), c.getRequestBody(), c.getPathVariables());
			break;
		case HEAD:
			responseEntity = restClient.head(c.buildUrl(), c.getPathVariables());
			break;
			
		//do nothing, use this method to represent a util case, like sleep to wait last case to finish
		//because WebAPI call may contain backend task which still run after response is returned
		case PATCH:
			responseEntity = new ResponseEntity<String>(HttpStatus.OK);
			break;
		default:
			throw new CaseExecutionException("request method is not supported ");		
		}
		
		if (responseEntity == null) {
			throw new CaseExecutionException("couldn't get any response");		
		}
		
		c.setResponseEntity(responseEntity);
		return true;
	}
}
