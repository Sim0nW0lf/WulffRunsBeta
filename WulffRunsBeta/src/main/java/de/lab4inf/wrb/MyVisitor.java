package de.lab4inf.wrb;

import java.util.ArrayList;
import java.util.LinkedList;

//import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import de.lab4inf.wrb.DemoBaseVisitor;
//import de.lab4inf.wrb.DemoParser.ExpressionContext;

public class MyVisitor extends DemoBaseVisitor<String> {
//	@Override
//	public String visitExpression(ExpressionContext ctx) {
//		visitChildren(ctx);
////		if(ctx.getChildCount() == 1) {
////			System.out.println(ctx.getChild(0));
////		}else {
////			System.out.println(ctx.getChild(2));
////			System.out.println("addition");
////		}
////		return null;
////		int ersteVariable = 0, zweiteVariable = 0;
////		if(ctx.getChildCount() == 1) {
////			ersteVariable = Integer.parseInt(ctx.getChild(0).getText());
////		}else {
////			zweiteVariable = Integer.parseInt(ctx.getChild(2).getText());
////		}
////		int a = ersteVariable+zweiteVariable;
////		System.out.println(a);
//		return null;
//	}
	
	//double solution;
	LinkedList<Double> solutionList = new LinkedList<Double>();
	
	public double getErgebnis() {
		return solutionList.getLast();
	}
	
	@Override public String visitExpression(DemoParser.ExpressionContext ctx) {
		for(int i = 0; i >= ctx.getChildCount(); i++) {
			if(!ctx.getChild(i).getText().equals(";")) {
				this.solutionList.add(rechnen(ctx.getChild(i)));
			}
		}
//		solution = 0;
//		double finalSolution = rechnen(ctx);
//		if(solution == 0)
//			solution = finalSolution;
//		System.out.println(solution);
		return null;
	}
	
	double rechnen(ParseTree ctx) {
		if(ctx.getChildCount() == 1) {
			double x = Double.parseDouble(ctx.getText());
//			System.out.println(x);
			return x;
		}else {
			switch(ctx.getChild(1).getText()){
				case "/": return rechnen(ctx.getChild(0)) / rechnen(ctx.getChild(2));
				case "*": return rechnen(ctx.getChild(0)) * rechnen(ctx.getChild(2));
				case "+": return rechnen(ctx.getChild(0)) + rechnen(ctx.getChild(2));
				case "-": return rechnen(ctx.getChild(0)) - rechnen(ctx.getChild(2));
				/*case ";": solution = rechnen(ctx.getChild(2));
//						solutionList.add(rechnen(ctx.getChild(0)));
						return 0;*/
				default:
					if(ctx.getChild(0).getText().equals("(")) {
						return rechnen(ctx.getChild(1));
					}
				return 0;
			}
		}
	}
}