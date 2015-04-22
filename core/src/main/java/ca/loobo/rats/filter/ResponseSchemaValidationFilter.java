package ca.loobo.rats.filter;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.MetaName;
import ca.loobo.rats.exceptions.CaseResponseValidationException;
import ca.loobo.rats.exceptions.ResourceNotFoundException;
import ca.loobo.rats.exceptions.ResourceNotReadableException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.Dereferencing;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class ResponseSchemaValidationFilter implements CaseFilter {

	@Override
	public void filter(Case c, CaseFilterChain chain) {
		validate(c);
		chain.doFilter(c);
	}

	@Override
	public String name() {
		return "SCHEMA_VALIDATION";
	}
	
	public void validate(Case c) {

		try {
			String responseSchema = getResponseSchema(c);
			if ( responseSchema != null) {
		        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
		                .dereferencing(Dereferencing.INLINE).freeze();
	            final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
		                .setLoadingConfiguration(cfg).freeze();

				JsonSchema schema = factory.getJsonSchema(JsonLoader
						.fromString(responseSchema));
				JsonNode jsonResponse = JsonLoader.fromString(c.getResponseBody());
				ProcessingReport report = schema.validate(jsonResponse);
				if (!report.isSuccess()) {
					throw new CaseResponseValidationException(report.toString());
				}
			}
		} catch (IOException e) {
			throw new CaseResponseValidationException("IOException: " + e.getMessage(), e);
		} catch (ProcessingException e) {
			throw new CaseResponseValidationException("ProcessingException: " + e.getMessage(), e);
		}

	}
	
	/**
	 * Schema file must be in the standard format schema_name.json
	 * @return
	 */
	@JsonIgnore
	final public String getResponseSchema(Case c) {
		
		String schemaPath = c.getMeta(MetaName.responseschema);
		if (StringUtils.isBlank(schemaPath)) {
			return null;
		}
		
		if (StringUtils.isEmpty(com.google.common.io.Files.getFileExtension(schemaPath))) {
			schemaPath += ".json";
		}
		
		Resource templateResource = c.getContext().getResource(schemaPath);
		if (templateResource == null) {
			throw new ResourceNotFoundException(schemaPath);
		}
		try {
			return IOUtils.toString(templateResource.getInputStream(), "UTF-8");
		} catch (IOException e) {
			throw new ResourceNotReadableException(templateResource.getFilename());
		}
	}	
}
