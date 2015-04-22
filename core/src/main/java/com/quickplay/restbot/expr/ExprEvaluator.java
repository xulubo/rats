package com.quickplay.restbot.expr;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.quickplay.restbot.exceptions.ExprAssertionException;
import com.quickplay.restbot.exceptions.ExprEvaluationException;
import com.quickplay.restbot.exceptions.ExprMethodNotFoundException;
import com.quickplay.restbot.exceptions.ExprParseException;
import com.quickplay.restbot.exceptions.ExprUnresolvableException;
import com.quickplay.restbot.expr.ExprParser.InvokeContext;
import com.quickplay.restbot.expr.ExprParser.SingleCompareContext;
import com.quickplay.restbot.expr.MethodRefFactory.MethodRef;

@Component
public class ExprEvaluator {
	static final Logger logger = LoggerFactory.getLogger(ExprEvaluator.class);
	
	@Resource
	MethodRefFactory exprEvaluatorFactory;

	public Object evaluate(MethodExpr method) {
		Object retVal = null;
		Object[] args = new Object[method.getParameters().size()];
		method.getParameters().toArray(args);
		MethodRef methodRef = exprEvaluatorFactory.getMethodRef(method.getInstanceName(), method.getMethodName(), args);
		if (methodRef == null) {
			throw new ExprEvaluationException("Method " + method.getMethodName() + " is not defined");
		}

		try {
			retVal = methodRef.method.invoke(methodRef.instance, args);
			logger.debug("result: " + retVal);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof ExprAssertionException) {
				throw (ExprAssertionException)e.getCause();
			}
			throw new ExprEvaluationException(e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ExprEvaluationException(e);
		} 

		return retVal;
	}
	
	public static void error(Token t, String msg) {
		logger.error("line %d:%d %s\n", t.getLine(), t.getCharPositionInLine(),
				msg);
	}

	public Object evaluate(String expr) {
		ExprParseListener exprParseListener = new ExprParseListener(this);
		return evaluate(expr, exprParseListener);
	}

	// simulate the expression like obj.expr
	public Object evaluate(Object obj, String expr) {
		ExprParseListener exprParseListener = new ExprParseListener(this, obj);
		return evaluate(expr, exprParseListener);
	}
	
	private Object evaluate(String expr, ExprParseListener exprParseListener)  {

		ANTLRInputStream input = new ANTLRInputStream(expr);
		ExprLexer lexer = new ExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.expr();
		// show tree in text form
		logger.debug("");
		logger.debug("--- Parse Tree {}", tree.toStringTree(parser));

		//currently only method invoke is supported
		if (!(tree instanceof InvokeContext) && !(tree instanceof SingleCompareContext)) {
			throw new ExprUnresolvableException("Unenvaluatable expression " + expr);
		}
		
		ParseTreeWalker walker = new ParseTreeWalker();
		
		try {
			walker.walk(exprParseListener, tree);
		} catch(ExprMethodNotFoundException | ExprParseException e) {
			//e.printStackTrace();
			throw new ExprUnresolvableException("no applicable method found for " + expr, e);
		}
		
		return exprParseListener.getResult(tree);
	}

}
