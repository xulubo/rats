package com.quickplay.restbot.expr;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.annotations.ExprHandler;
import com.quickplay.restbot.annotations.ExprMethod;

@Component
@ExprHandler
public class ExprOperatorHandler {
	static final Logger logger = LoggerFactory.getLogger(ExprOperatorHandler.class);
	
	@ExprMethod(alias="==")
	public Boolean operatorEqual(Object arg1, Object arg2) {
		logger.debug("eq: arg1:{} arg2:{}", arg1, arg2);
		return arg1.equals(arg2);
	}
	
	@ExprMethod(alias="!=")
	public Boolean operatorNotEqual(Object arg1, Object arg2) {
		logger.debug("ne: arg1:{} arg2:{}", arg1, arg2);
		return !arg1.equals(arg2);
	}
	
	@ExprMethod(alias=">=")
	public Boolean operatorGreaterOrEqual(Integer arg1, Object arg2) {
		long left = ((Integer)arg1).longValue();
		long right = Integer.valueOf(arg2.toString());
		return left >= right;		
	}
	
	@ExprMethod(alias=">=")
	public Boolean operatorGreaterOrEqual(Long arg1, Object arg2) {
		long left = ((Long)arg1).longValue();
		long right = Long.valueOf(arg2.toString());
		return left >= right;		
	}

	@ExprMethod(alias=">=")
	public Boolean operatorGreaterOrEqual(Double arg1, Object arg2) {
		double left = ((Double)arg1).doubleValue();
		double right = Double.valueOf(arg2.toString());
		return left >= right;
	}
	
	@ExprMethod(alias=">")
	public Boolean operatorGreater(Integer arg1, Object arg2) {
		long left = ((Integer)arg1).longValue();
		long right = Integer.valueOf(arg2.toString());
		return left > right;	
	}
	
	@ExprMethod(alias=">")
	public Boolean operatorGreater(Long arg1, Object arg2) {
		long left = ((Long)arg1).longValue();
		long right = Long.valueOf(arg2.toString());
		return left > right;	
	}

	@ExprMethod(alias=">")
	public Boolean operatorGreater(Double arg1, Object arg2) {
		double left = ((Double)arg1).doubleValue();
		double right = Double.valueOf(arg2.toString());
		return left > right;
	}	
	
	@ExprMethod(alias="<=")
	public Boolean operatorLessOrEqual(Integer arg1, Object arg2) {
		int left = ((Integer)arg1).intValue();
		int right = Integer.valueOf(arg2.toString());
		return left <= right;
	}
	
	@ExprMethod(alias="<=")
	public Boolean operatorLessOrEqual(Long arg1, Object arg2) {
		long left = ((Long)arg1).longValue();
		long right = Long.valueOf(arg2.toString());
		return left <= right;
	}
	
	@ExprMethod(alias="<=")
	public Boolean operatorLessOrEqual(Double arg1, Object arg2) {
		double left = ((Double)arg1).doubleValue();
		double right = Double.valueOf(arg2.toString());
		return left <= right;
	}
	
	@ExprMethod(alias="<")
	public Boolean operatorLess(Integer arg1, Object arg2) {
		long left = ((Integer)arg1).longValue();
		long right = Integer.valueOf(arg2.toString());
		return left < right;	
	}
	
	@ExprMethod(alias="<")
	public Boolean operatorLess(Long arg1, Object arg2) {
		long left = ((Long)arg1).longValue();
		long right = Long.valueOf(arg2.toString());
		return left < right;	
	}

	@ExprMethod(alias="<")
	public Boolean operatorLess(Double arg1, Object arg2) {
		double left = ((Double)arg1).doubleValue();
		double right = Double.valueOf(arg2.toString());
		return left < right;
	}

	@ExprMethod
	public Boolean between(Integer value, Object min, Object max) {value.doubleValue();
		return between(value.doubleValue(), min, max);
	}
	
	@ExprMethod
	public Boolean between(Long value, Object min, Object max) {
		return between(value.doubleValue(), min, max);
	}
	
	@ExprMethod
	public Boolean between(Double value, Object min, Object max) {
		Asserts.notNull(value, "value can't be null");
		Asserts.notNull(min, "min can't be null");
		Asserts.notNull(max, "max can't be null");

		try {
			double dmin = Double.valueOf(min.toString());
			double dmax = Double.valueOf(max.toString());
			return value.doubleValue() >= dmin && value.doubleValue() <= dmax;

		} catch(Throwable t) {
			throw new RuntimeException("failed in between", t);
		}
		
	}	
}
