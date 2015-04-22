// Generated from Expr.g4 by ANTLR 4.3
package com.quickplay.restbot.expr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprParser}.
 */
public interface ExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code Invoke}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInvoke(@NotNull ExprParser.InvokeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Invoke}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInvoke(@NotNull ExprParser.InvokeContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExprParser#str}.
	 * @param ctx the parse tree
	 */
	void enterStr(@NotNull ExprParser.StrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#str}.
	 * @param ctx the parse tree
	 */
	void exitStr(@NotNull ExprParser.StrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code SingleCompare}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSingleCompare(@NotNull ExprParser.SingleCompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleCompare}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSingleCompare(@NotNull ExprParser.SingleCompareContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull ExprParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull ExprParser.NumberContext ctx);

	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterString(@NotNull ExprParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitString(@NotNull ExprParser.StringContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Var}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVar(@NotNull ExprParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVar(@NotNull ExprParser.VarContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompare(@NotNull ExprParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompare(@NotNull ExprParser.CompareContext ctx);

	/**
	 * Enter a parse tree produced by {@link ExprParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(@NotNull ExprParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(@NotNull ExprParser.ExprListContext ctx);
}