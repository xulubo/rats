package com.quickplay.restbot.expr;

import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quickplay.restbot.exceptions.ExprParseException;
import com.quickplay.restbot.expr.ExprParser.CompareContext;
import com.quickplay.restbot.expr.ExprParser.ExprListContext;
import com.quickplay.restbot.expr.ExprParser.InvokeContext;
import com.quickplay.restbot.expr.ExprParser.NumberContext;
import com.quickplay.restbot.expr.ExprParser.SingleCompareContext;
import com.quickplay.restbot.expr.ExprParser.StringContext;
import com.quickplay.restbot.expr.ExprParser.VarContext;


public class ExprParseListener extends ExprBaseListener {
	
	static final Logger logger = LoggerFactory.getLogger(ExprParseListener.class);
	
    ParseTreeProperty<Object> resultTree = new ParseTreeProperty<Object>();
    MethodExpr methodExpr = null;
    LinkedList<MethodExpr> stack = new LinkedList<MethodExpr>();

    ExprEvaluator exprEvaluator;
	Object firstParameter;
	
	public ExprParseListener(ExprEvaluator exprEvaluator) {
		this.exprEvaluator = exprEvaluator;
	}
	
	public ExprParseListener(ExprEvaluator exprEvaluator, Object firstParameter) {
		this.exprEvaluator = exprEvaluator;
		this.firstParameter = firstParameter;
	}
	
	//single comparison can never be used as a parameter of method, so no need to add in stack
	@Override
    public void exitSingleCompare(SingleCompareContext ctx) {
    	String opr = ctx.getChild(0).getText();
    	if (this.firstParameter == null) {
    		throw new ExprParseException("Left number was not found for operator " + opr);
    	}
    	MethodExpr mi = new MethodExpr();
    	mi.setMethodName(opr);
    	mi.addParameter(this.firstParameter);
    	if (ctx.NUMBER().size() == 1) {
        	mi.addParameter(ctx.NUMBER(0).getText());
    	} else {
        	mi.addParameter(ctx.NUMBER(0).getText() + "." + ctx.NUMBER(1));
    	}
    	
		Object val = exprEvaluator.evaluate(mi);
		resultTree.put(ctx, val);
	}
    
    @Override
    public void enterInvoke(InvokeContext ctx) {
    	MethodExpr mi = new MethodExpr();
    	if (ctx.ID().size() == 1) {
        	mi.setMethodName(ctx.ID().get(0).getText());    		
    	}
    	else {
        	mi.setInstanceName(ctx.ID().get(0).getText());    		
    		mi.setMethodName(ctx.ID().get(1).getText());
    	}
    	
    	if (methodExpr != null) {
    		stack.push(methodExpr);
    	}
    	else if (firstParameter != null){
    		//for handling case like obj.expression
    		mi.addParameter(firstParameter);
    	}
    	
    	methodExpr = mi;
    }
    
	@Override
	public void exitInvoke(InvokeContext ctx) {
		Object val = exprEvaluator.evaluate(methodExpr);
		resultTree.put(ctx, val);
		if (stack.size()>0) {
			methodExpr = stack.pop();
			if (methodExpr != null) {
				methodExpr.addParameter(val);
			}
		}
		else {
			methodExpr = null;
		}
	}

	@Override
	public void enterCompare(CompareContext ctx) {
    	MethodExpr mi = new MethodExpr();
    	String operator = ctx.getChild(1).getText();
    	mi.setMethodName(operator);
  	
    	if (methodExpr != null) {
    		stack.push(methodExpr);
    	}
    	methodExpr = mi;
	}
	
	@Override
	public void exitCompare(CompareContext ctx) {
		Object val = exprEvaluator.evaluate(methodExpr);
		resultTree.put(ctx, val);
		if (stack.size()>0) {
			methodExpr = stack.pop();
			if (methodExpr != null) {
				methodExpr.addParameter(val);
			}
		}
		else {
			methodExpr = null;
		}
	}

	@Override
	public void exitVar(VarContext ctx) {
		if (methodExpr != null) {
			methodExpr.addParameter(ctx.ID().getText());
		}
	}

	@Override
	public void enterExprList(ExprListContext ctx) {
		
	}

	@Override
	public void exitString(StringContext ctx) {
		String str = ctx.getChild(1).getText();
		logger.debug("string --- : <{}>", str);
		if (methodExpr != null) {
			methodExpr.addParameter(str);
		}
	}
		
	@Override
	public void exitNumber(NumberContext ctx) {
		if (methodExpr != null) {
			System.err.println(ctx.getText());
			methodExpr.addParameter(Long.valueOf(ctx.getText()));
		}
	}

	public Object getResult(ParseTree tree) {
		return resultTree.get(tree);
	}
}
