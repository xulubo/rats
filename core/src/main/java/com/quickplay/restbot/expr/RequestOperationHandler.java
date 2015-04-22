package com.quickplay.restbot.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.annotations.ExprHandler;
import com.quickplay.restbot.annotations.ExprMethod;
import com.quickplay.restbot.caze.CaseSessionHolder;
import com.quickplay.restbot.utils.BeforeRequestStartListener;
import com.quickplay.restbot.utils.RestClient;

@Component
@ExprHandler("req")
public class RequestOperationHandler {
	static final Logger logger = LoggerFactory.getLogger(RequestOperationHandler.class);
	
	@ExprMethod
	public void header(final String name, final String value) {
		BeforeRequestStartListener l = new BeforeRequestStartListener() {

			@Override
			public void onRequest(RestClient client) {
				client.addRequestHeader(name, value);
			}
			
		};
		CaseSessionHolder.currentSession().setAttribute(BeforeRequestStartListener.class.getName(), l);
	}


}
