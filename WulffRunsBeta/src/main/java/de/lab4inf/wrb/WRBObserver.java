package de.lab4inf.wrb;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import de.lab4inf.wrb.DemoLexer;
import de.lab4inf.wrb.DemoParser;
import de.lab4inf.wrb.DemoBaseVisitor;
//import de.lab4inf.wrb.MyVisitor;

//import de.lab4inf.wrb.DemoParser.ExpressionContext;

public class WRBObserver extends DemoBaseVisitor<String> {
	public static void main(String[] args) throws Exception {
		CharStream input = CharStreams.fromFileName("code.demo");
		DemoLexer lexer = new DemoLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DemoParser parser = new DemoParser(tokens);
		
		ParseTree tree = parser.expression();
		new MyVisitor().visit(tree);
	}

	
//	@Override
//	public void exitExpression(ExpressionContext ctx) {
//		
//		int k = 0;
//		double value = getValue(ctx.children(k));
//		ParseTree node = ctx.multi(++k);
//		while (null != node) {
//			Token op = ctx.operator.get(k - 1);
//			if (WRBLexer.ADD == op.getType()) {
//				value += getValue(node);
//			} else {
//				value -= getValue(node);
//			}
//			node = ctx.multi(++k);
//		}
//		setValue(ctx, value);
//
//		lastValue = value;
//		// debug("last value is" + lastValue);
//	}
}
