package de.lab4inf.wrb;

import java.util.HashMap;
//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Map;

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
//	LinkedList<Variable> varList = new LinkedList<Variable>();
	HashMap<String, Variable> varMap = new HashMap<String, Variable>();
	
	/**
	 * @return the last Entry to solutionList, the most recent solved equation. 
	 */
	public double getErgebnis() {
		return solutionList.getLast();
	}
	
	/* (non-Javadoc)
	 * @see de.lab4inf.wrb.DemoBaseVisitor#visitRoot(de.lab4inf.wrb.DemoParser.RootContext)
	 */
	@Override public String visitRoot(DemoParser.RootContext ctx){
		this.solutionList.clear();
//		this.varList.clear();
		this.varMap.clear();
		
		if(ctx.getParent() == null)
		for(int i = 0; i < ctx.getChildCount(); i++) {
			if(!ctx.getChild(i).getText().equals(";")) {
				this.solutionList.add(rechnen(ctx.getChild(i)));
			}
		}
//		solution = 0;
//		double finalSolution = rechnen(ctx);
//		if(solution == 0)
//			solution = finalSolution;
//		System.out.println(solution);
//		
		
		
		return null;
	}
	
	/**
	 * Recursive Function that does the bulk of the math work
	 * @param ctx current element of Tree
	 * @return solution to current Element
	 */
	double rechnen(ParseTree ctx) {
		double x = 0;
		switch(ctx.getChildCount()) {
		case 1: //either a wrapper or simple Number/Variable
			if(ctx.getChild(0).getChildCount() > 0) {
				return rechnen(ctx.getChild(0));
			}
			try {
				x = Double.parseDouble(ctx.getText());
			} catch (Exception e) {
				if(this.varMap.containsKey(ctx.getText())) {
					x = this.varMap.get(ctx.getText()).getValue(); 
				} else {
					x = 0;
					System.out.println("Unknown Variable: " + ctx.getText() + ". \n");
				}
				/*try {
					v = getVariable(ctx.getText());
				} catch (IllegalArgumentException e2) {
					x = 0;
					System.out.println("Unable to map Variable to " + ctx.getText() + ". \n"+ e2.getMessage() + "\n\n");
				}*/
			}
			
//			System.out.println(x);
			return x;
		case 3: //simple mathematical Equation or Declaration
			switch(ctx.getChild(1).getText()){
			case "/": return rechnen(ctx.getChild(0)) / rechnen(ctx.getChild(2));
			case "*": return rechnen(ctx.getChild(0)) * rechnen(ctx.getChild(2));
			case "+": return rechnen(ctx.getChild(0)) + rechnen(ctx.getChild(2));
			case "-": return rechnen(ctx.getChild(0)) - rechnen(ctx.getChild(2));
			case "^":
			case "**": return Math.pow(rechnen(ctx.getChild(0)), rechnen(ctx.getChild(2)));
			case "e": return rechnen(ctx.getChild(0)) * Math.pow(10, rechnen(ctx.getChild(2)));
			/*case ";": solution = rechnen(ctx.getChild(2));
//					solutionList.add(rechnen(ctx.getChild(0)));
					return 0;*/
			case "%": 
			case "mod": return rechnen(ctx.getChild(0)) % rechnen(ctx.getChild(2));
			case "=": 
				x = rechnen(ctx.getChild(2));
				this.varMap.put(ctx.getChild(0).getText(), (new Variable(ctx.getChild(0).getText(), x)));
				//this.varList.add(new Variable(ctx.getChild(0).getText(), x));
				return x;
			default: //Bracketed expression
				if(ctx.getChild(0).getText().equals("(")) {
					return rechnen(ctx.getChild(1));
				}
			}
			break;
		case 6: //Function
			break;
		default: 
			System.out.println("Unknown Tree case. \n");
			return -1;
		}
		return -10;
	}
	
	
	
	
	
	
	/**
	 * @param varName Name of the Variable to search
	 * @return the Variable with matching name
	 * @throws IllegalArgumentException the searched Variable does not exist
	 */
	public Variable getVariable(String varName) throws IllegalArgumentException {
		/*Variable v = varList.stream()
				  .filter(w -> varName.equals(w.getName()))
				  .findAny()
				  .orElse(null);
		if(v == null) {
			throw new IllegalArgumentException("Error 404: Variable not Found");
		}*/
		
		if(!this.varMap.containsKey(varName)) {
			throw new IllegalArgumentException("Error 404: Variable "+varName+" not Found");
		} else {
			return this.varMap.get(varName);
		}
//		return v;
	}
	
	/**
	 * @param varName the Name of the Variable
	 * @param varValue the Value of the Variable
	 */
	public void setVariable(String varName, double varValue) {
		Variable v;
		try {
			v = getVariable(varName);
			v.setValue(varValue);
		} catch (IllegalArgumentException e) {
			this.varMap.put(varName, new Variable(varName, varValue));
			//this.varList.add(new Variable(varName, varValue));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}