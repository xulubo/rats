package ca.loobo.rats.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;
import ca.loobo.rats.caze.CaseSessionHolder;

@Component
@ExprHandler("db")
public class DBOperationHandler {
	static final Logger logger = LoggerFactory.getLogger(DBOperationHandler.class);
	
	@ExprMethod
	public Object query(final String sql) {

		JdbcTemplate jdbcTemplate = CaseSessionHolder.currentSession().getContext().getJdbcTemplate();
		return jdbcTemplate.queryForObject(sql, Object.class);
	}


}
