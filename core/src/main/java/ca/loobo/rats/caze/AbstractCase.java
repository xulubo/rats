package ca.loobo.rats.caze;

import static ca.loobo.rats.caze.MetaName.commonparams;
import static ca.loobo.rats.caze.MetaName.id;
import static ca.loobo.rats.caze.MetaName.method;
import static ca.loobo.rats.caze.MetaName.path;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import ca.loobo.rats.exceptions.CaseParseException;
import ca.loobo.rats.param.CollectionParamValue;
import ca.loobo.rats.param.DefaultParamValue;
import ca.loobo.rats.utils.ParamUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

public abstract class AbstractCase {
	final static Logger logger = LoggerFactory.getLogger(AbstractCase.class);

	private TestContext context;
	private TestCaseResult result = new TestCaseResult();
	private ResponseEntity<String> responseEntity;				// response from catalog-api
	protected Map<String, String> metas;						// meta info from resource file
	protected Map<String, ParamValue> queryParams;					// used to generate the Url pattern
	protected Map<String, String> pathVariables;				// used to generate the Url pattern
	protected Map<String, ParamValue> expectations;
	private String url;

	public AbstractCase(TestContext ctx) {
		this.context = ctx;
		this.metas = Maps.newLinkedHashMap();
		this.queryParams = Maps.newLinkedHashMap();
		this.expectations = Maps.newLinkedHashMap();
		this.pathVariables = Maps.newLinkedHashMap();
	}

	public TestCaseResult result() {
		return result;
	}

	public void setMeta(String metaName, String value) {
		try {
			String fieldName = metaName.toLowerCase();
			this.metas.put(fieldName, value);
			Field field = this.getClass().getField(fieldName);
			field.set(this, value);
		} catch (Exception e) {
			//ignore
		} 
	}

	public String getId() {
		return getMeta(id);
	}

	public final String getMeta(MetaName metaName) {
		return this.metas.get(metaName.name());
	}
	
	public final String getMeta(String metaName) {
		return this.metas.get(metaName);
	}	

	public final ParamValue getQueryParameter(String name) {
		ParamValue v = this.queryParams.get(name);
		return v == null ? new DefaultParamValue() : v;
	}

	public final String getPathVariable(String name) {
		return this.pathVariables.get(name);
	}

	public final ParamValue getExpectation(String name) {
		ParamValue v = this.expectations.get(name);
		return v == null ? new DefaultParamValue() : v;
	}
	
	public final void addExpectation(String name, String value) {
		this.expectations.put(name, new DefaultParamValue(value));
	}


	public final String getResponseBody() {
		if (this.responseEntity == null) {
			return null;
		}

		return this.responseEntity.getBody();
	}

	@JsonIgnore
	public ResponseEntity<String> getResponseEntity() {
		return this.responseEntity;
	}

	public void setResponseEntity(ResponseEntity<String> responseEntity) {
		this.responseEntity = responseEntity;
	}

	final public Map<String, String> getAllVaribles() {
		Map<String, String> vars = Maps.newHashMap();
		//extend with global parameters
		vars.putAll(getContext().getParamMap());
		vars.putAll(this.pathVariables);
		
		for(Entry<String, ParamValue> entry : this.queryParams.entrySet()) {
			vars.put(entry.getKey(), entry.getValue().toString());
		}

		if (this.getResponseEntity() != null) {
			HttpHeaders headers = this.getResponseEntity().getHeaders();
			for(String name : headers.keySet()) {
				vars.put("H." + name, "\"" + headers.getFirst(name) + "\"");
			}
		}

		return vars;
	}
	
	final public String buildUrl() {
		if (this.url == null) {
			String p = getMeta(path);
			UriComponentsBuilder builder = context.getUriBuilder();
			if (StringUtils.isNotBlank(p)) {
				p = p.trim();
				p = ParamUtils.extend(p, this.pathVariables);
				//separate query string from path
				String parts[] = p.split("\\?", 2);
				builder.path(parts[0]);
				if (parts.length>1) {
					builder.query(parts[1]);
				}
			}

			p=getMeta(commonparams);
			if (StringUtils.isNotBlank(p)) {
				p = p.trim();
				builder.query(ca.loobo.rats.utils.ParamUtils.extend(p, context.getParamMap()));
			}

			for(Entry<String, ParamValue> pair : this.queryParams.entrySet()) {
				String name = pair.getKey();
				String value = pair.getValue().resolve();

				builder.queryParam(name, value);
			}

			this.url = builder.build(false).toUriString();
			this.url = ParamUtils.extend(url, this.context.getParamMap());
		}

		return url;
	}

	final public HttpMethod getRequestMethod() {
		String requestMethod = getMeta(method);
		if (StringUtils.isBlank(requestMethod)) {
			return HttpMethod.GET;
		}

		try {
			return HttpMethod.valueOf(requestMethod.toUpperCase());
		}
		catch (Throwable t){
			throw new CaseParseException("Illegal method " + requestMethod, t);
		}
	}

	@JsonIgnore
	final public TestContext getContext() {
		return this.context;
	}

	/**
	 * Is this a case with any ranged parameter inside
	 * @return
	 */
	final public boolean isCollectionCase() {
		for(ParamValue pv : this.queryParams.values()) {
			if (pv instanceof CollectionParamValue) {
				return true;
			}
		}

		return false;
	}

}
