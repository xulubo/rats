package ca.loobo.rats.expr;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;

import ca.loobo.rats.AppContext;
import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;
import ca.loobo.rats.app.ExprRuntime;
import ca.loobo.rats.expr.ExprEvaluator;

@Component
@ExprHandler
public class ExprTest {
	
	String STR = "\\\"\\a!b\\\"c de-f,g. *\\\\";

	@Resource
	ExprEvaluator exprEvaluator;

	@Before
	public void init() {
		AppContext.autowireBean(this);
	}
	
	@Test
	public void test() throws Exception {
		//new ExprExecutor().process("eq(lt(ok(a)),b,c)");
		
		ExprRuntime r = new ExprRuntime();
		r.evaluate("eq(-1, -1)");
		r.evaluate("ne(1, 2)");
//		r.evaluate("bod.completed1(1)");
//		r.evaluate("3==\"aa 123~#adf @#$cd ~23?!\"");
//		r.evaluate("\"3\"==\"3\"");
//		r.evaluate("true(3==3)");
		//r.evaluate("test(3)");
		//r.evaluate("test(\"abcd\")");
		r.evaluate("  test(\"" + STR + "\", 3,4)  ");

	}
	
//	@Test
	public void testSingleCompare() {
		exprEvaluator.evaluate(-2, "<100");
		exprEvaluator.evaluate(2, "<100");
		exprEvaluator.evaluate(2, ">1");
		exprEvaluator.evaluate(2L, "<100");
		exprEvaluator.evaluate(2L, ">1");
		exprEvaluator.evaluate(2.0, ">1");
		exprEvaluator.evaluate(2.0, "<3");
		exprEvaluator.evaluate(2.0, ">1.1");
		exprEvaluator.evaluate(2.0, "<3.1");
	}

	@ExprMethod
	public void test(Object a, Object b, Object c) {
		System.err.println(STR);
		System.err.println(a);
		Assert.assertEquals(STR, a);
		System.err.println("test a=" + a + " b=" + b + " c=" + c);
	}
}
