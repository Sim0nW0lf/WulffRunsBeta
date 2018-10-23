// Generated from Demo.g4 by ANTLR 4.4
package de.lab4inf.wrb;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DemoParser}.
 */
public interface DemoListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DemoParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull DemoParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DemoParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull DemoParser.ExpressionContext ctx);
}