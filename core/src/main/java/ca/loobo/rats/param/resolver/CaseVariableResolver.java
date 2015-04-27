package ca.loobo.rats.param.resolver;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.CaseSessionHolder;
import ca.loobo.rats.caze.TestContext;
import ca.loobo.rats.utils.ParamUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

/**
 * Resolve variables which are defined in the case scope, 
 * like queryParams, responseHeaders(in the format of H.headerName), 
 * responseBody(in json format), responseExpectations, metaFields
 * 
 * value is in the format of [$]{[caseId:]valName}	([] indicates optional)
 * 
 * value can be any string containing a variable like ${var1} or hello${var1}
 * @author robertx
 *
 */
public class CaseVariableResolver implements ValueResolver {

	@Override
	public String resolve(String value, ValueResolverChain chain) {
		return chain.resolve(doResolve(value));
	}
	
	private String doResolve(String value) {
		TestContext context = CaseSessionHolder.getContext();
		Map<String, String> params = Maps.newHashMap();
		Collection<String> varNames = ParamUtils.extractVariableNames(value);
		if (varNames.size()>0) {
			for(String varName : varNames) {
				Case caze; String valName;
				List<String> parts = Splitter.on(":").limit(2).splitToList(varName);
				if (parts.size() == 2) {
					caze = context.getCase(parts.get(0));
					valName = parts.get(1);
				}
				else {
					caze = CaseSessionHolder.currentSession().getCase();
					valName = varName;
				}
				params.put(varName, context.readCaseValue(caze, valName).toString());
			}

			value = ParamUtils.extend(value, params);
			if (ParamUtils.isExtendable(value)) {
				value = doResolve(value);
			}
			
			//parameter value can be also a variable or variable contained string
			//so need to do future check
			return doResolve(value);
		}

		return value;
	}


}
