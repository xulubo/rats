package ca.loobo.rats.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;
import ca.loobo.rats.caze.CaseSessionHolder;
import ca.loobo.rats.utils.BeforeRequestStartListener;
import ca.loobo.rats.utils.RestClient;

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
