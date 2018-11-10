package de.lab4inf.wrb;

import java.util.HashMap;
import java.util.Iterator;
//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Map;
import java.util.stream.Stream;

import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.misc.NotNull;
//import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;


import de.lab4inf.wrb.DemoBaseVisitor;
//import de.lab4inf.wrb.DemoParser.ExpressionContext;

public class MyVisitor extends DemoBaseVisitor<Double> {
	// @Override
	// public String visitExpression(ExpressionContext ctx) {
	// visitChildren(ctx);
	//// if(ctx.getChildCount() == 1) {
	//// System.out.println(ctx.getChild(0));
	//// }else {
	//// System.out.println(ctx.getChild(2));
	//// System.out.println("addition");
	//// }
	//// return null;
	//// int ersteVariable = 0, zweiteVariable = 0;
	//// if(ctx.getChildCount() == 1) {
	//// ersteVariable = Integer.parseInt(ctx.getChild(0).getText());
	//// }else {
	//// zweiteVariable = Integer.parseInt(ctx.getChild(2).getText());
	//// }
	//// int a = ersteVariable+zweiteVariable;
	//// System.out.println(a);
	// return null;
	// }

	// double solution;
	LinkedList<Double> solutionList = new LinkedList<Double>();
	// LinkedList<Variable> varList = new LinkedList<Variable>();
	HashMap<String, Variable> varMap = new HashMap<String, Variable>();
	HashMap<String, Function> funcMap = new HashMap<String, Function>();

	/**
	 * @return the last Entry to solutionList, the most recent solved equation.
	 */
	public double getErgebnis() {
		return solutionList.getLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.wrb.DemoBaseVisitor#visitRoot(de.lab4inf.wrb.DemoParser.
	 * RootContext)
	 */
	@Override
	public Double visitRoot(DemoParser.RootContext ctx) {
		// this.solutionList.clear();
		// this.varList.clear();
		// this.varMap.clear();
		Double s = visitChildren(ctx);

//		if (ctx.getParent() == null)
//			for (int i = 0; i < ctx.getChildCount(); i++) {
//				if (!ctx.getChild(i).getText().equals(";")) {
//					this.solutionList.add(rechnen(ctx.getChild(i)));
//				}
//			}
		// solution = 0;
		// double finalSolution = rechnen(ctx);
		// if(solution == 0)
		// solution = finalSolution;
		// System.out.println(solution);

//		System.out.println(getErgebnis());
		return s;
	}

	public Double visitStatement(DemoParser.StatementContext ctx) {
		Double d = visitChildren(ctx);
		this.solutionList.add(d);
		return d;
	}
	
	/**
	 * Recursive Function that does the bulk of the math work
	 * 
	 * @param ctx current element of Tree
	 * @return solution to current Element
	 */
	double rechnen(ParseTree ctx) {
		double x = 0;
		switch (ctx.getChildCount()) {
		case 0: // simple Number/Variable
			try {
				x = Double.parseDouble(ctx.getText());
			} catch (Exception e) {
				if (this.varMap.containsKey(ctx.getText())) {
					x = this.varMap.get(ctx.getText()).getValue();
				} else {
					throw new IllegalArgumentException("Unknown Variable: " + ctx.getText() + ". \n");
				}
				/*
				 * try { v = getVariable(ctx.getText()); } catch (IllegalArgumentException e2) {
				 * x = 0; System.out.println("Unable to map Variable to " + ctx.getText() +
				 * ". \n"+ e2.getMessage() + "\n\n"); }
				 */
			}

			// System.out.println(x);
			return x;
		case 1: // a wrapper
			// if(ctx.getChild(0).getChildCount() > 0) {
			return rechnen(ctx.getChild(0));
		// }
		// System.out.println(x);
		// return x;
		case 2: // signed numbers
			if (ctx.getChild(0).getText().equals("-")) {
				return rechnen(ctx.getChild(1)) * -1;
			} else if (ctx.getChild(0).getText().equals("+")) {
				return rechnen(ctx.getChild(1));
			}
			return 0;
		case 3: // simple mathematical Equation or Declaration
			switch (ctx.getChild(1).getText()) {
			case "/":
				return rechnen(ctx.getChild(0)) / rechnen(ctx.getChild(2));
			case "*":
				return rechnen(ctx.getChild(0)) * rechnen(ctx.getChild(2));
			case "+":
				return rechnen(ctx.getChild(0)) + rechnen(ctx.getChild(2));
			case "-":
				return rechnen(ctx.getChild(0)) - rechnen(ctx.getChild(2));
			case "^":
			case "**":
				return Math.pow(rechnen(ctx.getChild(0)), rechnen(ctx.getChild(2)));
			case "e":
				return rechnen(ctx.getChild(0)) * Math.pow(10, rechnen(ctx.getChild(2)));
			/*
			 * case ";": solution = rechnen(ctx.getChild(2)); //
			 * solutionList.add(rechnen(ctx.getChild(0))); return 0;
			 */
			case "%":
			case "mod":
				return rechnen(ctx.getChild(0)) % rechnen(ctx.getChild(2));
			case "=":
				x = rechnen(ctx.getChild(2));
				this.varMap.put(ctx.getChild(0).getText(), (new Variable(ctx.getChild(0).getText(), x)));
				// this.varList.add(new Variable(ctx.getChild(0).getText(), x));
				return x;
			default: // Bracketed expression
				if (ctx.getChild(0).getText().equals("(")) {
					return rechnen(ctx.getChild(1));
				}
			}
			break;
		case 4: // Function call
			if (ctx.getChild(1).getText().equals("(") && ctx.getChild(3).getText().equals(")")) {
				// Check if function is defined
				if (this.funcMap.containsKey(ctx.getChild(0).getText())) {
					LinkedList<Double> args = new LinkedList<Double>();
					// Extract all those juicy arguments
					for (int i = 0; i < ctx.getChild(2).getChildCount(); i++) {
						if (!ctx.getChild(2).getChild(i).getText().equals(",")) {
							args.add(rechnen(ctx.getChild(2).getChild(i)));
						}
					}
					Double[] t = args.toArray(new Double[args.size()]);
					return this.funcMap.get(ctx.getChild(0).getText())
							.eval(Stream.of(t).mapToDouble(Double::doubleValue).toArray());
				}

				// TODO: find a way to fu*** wrap the whole Math class like this sh*t
				switch (ctx.getChild(0).getText()) {
				case "sin":
					return Math.sin(rechnen(ctx.getChild(2)));
				case "cos":
					return Math.cos(rechnen(ctx.getChild(2)));
				case "tan":
					return Math.tan(rechnen(ctx.getChild(2)));
				case "asin":
					return Math.asin(rechnen(ctx.getChild(2)));
				case "acos":
					return Math.acos(rechnen(ctx.getChild(2)));
				case "atan":
					return Math.atan(rechnen(ctx.getChild(2)));
				case "sinh":
					return Math.sinh(rechnen(ctx.getChild(2)));
				case "cosh":
					return Math.cosh(rechnen(ctx.getChild(2)));
				case "tanh":
					return Math.tanh(rechnen(ctx.getChild(2)));
				case "log": // this shouldn't be here, but at ln. well, whatever the profs want i guess
				case "log10":
					return Math.log10(rechnen(ctx.getChild(2)));
				case "ln":
					return Math.log(rechnen(ctx.getChild(2)));
				case "lb":
				case "ld":
				case "log2":
					return Math.log(rechnen(ctx.getChild(2))) / Math.log(2);
				case "logE":
					return Math.log(rechnen(ctx.getChild(2))) / Math.log(Math.E);
				case "abs":
					return Math.abs(rechnen(ctx.getChild(2)));
				case "exp":
					return Math.exp(rechnen(ctx.getChild(2)));
				case "sqrt":
					return Math.sqrt(rechnen(ctx.getChild(2)));
				case "min":
					double min = rechnen(ctx.getChild(2).getChild(0));
					for (int i = 1; i < ctx.getChild(2).getChildCount(); i++) {
						if (!ctx.getChild(2).getChild(i).getText().equals(",")) {
							min = Math.min(min, rechnen(ctx.getChild(2).getChild(i)));
						}
					}
					return min;
				case "max":
					double max = rechnen(ctx.getChild(2).getChild(0));
					for (int i = 1; i < ctx.getChild(2).getChildCount(); i++) {
						if (!ctx.getChild(2).getChild(i).getText().equals(",")) {
							max = Math.max(max, rechnen(ctx.getChild(2).getChild(i)));
						}
					}
					return max;
				case "pow":
					double pow = rechnen(ctx.getChild(2).getChild(0));
					for (int i = 1; i < ctx.getChild(2).getChildCount(); i++) {
						if (!ctx.getChild(2).getChild(i).getText().equals(",")) {
							pow = Math.pow(pow, rechnen(ctx.getChild(2).getChild(i)));
						}
					}
					return pow;
				case "-":
					return rechnen(ctx.getChild(2)) * -1;
				}

				throw new IllegalArgumentException("Unknown Function called: " + ctx.getText());
			}
		case 6: // Function declaration or 2-arguments call
				// Check if this is a definition
//			if (ctx.getChild(4).getText().equals("=")) {
////				if (!this.funcMap.containsKey(ctx.getChild(0).getText())) {
//				MyFunction f = new MyFunction(ctx, this);
//				this.funcMap.put(ctx.getChild(0).getText(), f);
////				}
			return 0;
//			}

		default:
			throw new IllegalArgumentException(
					"\n Unknown Tree case: " + ctx.getChildCount() + ": " + ctx.getText() + " \n");
		}
		return -10;
	}

	@Override
	public Double visitFunctionDefinition(DemoParser.FunctionDefinitionContext ctx) {
		MyFunction f = new MyFunction(ctx, this);
		this.funcMap.put(ctx.name.getText(), f);
		return 0.0;
	}

	@Override
	public Double visitAssignment(DemoParser.AssignmentContext ctx) {
		Double d = visit(ctx.expression());
		this.varMap.put(ctx.VARIABLE().getText(), new Variable(ctx.VARIABLE().getText(), d));
		return d;
	}

	@Override
	public Double visitDivision(@NotNull DemoParser.DivisionContext ctx) {
		return visit(ctx.links) / visit(ctx.rechts);
	}

	@Override
	public Double visitMultiplikation(@NotNull DemoParser.MultiplikationContext ctx) {
		return visit(ctx.links) * visit(ctx.rechts);
	}

	@Override
	public Double visitSubtraktion(@NotNull DemoParser.SubtraktionContext ctx) {
		return visit(ctx.links) - visit(ctx.rechts);
	}

	@Override
	public Double visitAddition(@NotNull DemoParser.AdditionContext ctx) {
		return visit(ctx.links) + visit(ctx.rechts);
	}

	@Override
	public Double visitNumber(@NotNull DemoParser.NumberContext ctx) {
		Double d = Double.parseDouble(ctx.NUMBER().getText()); 
		if (ctx.getChildCount() > 1 && ctx.sign.getType() == DemoParser.SUB) {
			d *= -1;
		}
		return d;
	}

	@Override
	public Double visitModulo(@NotNull DemoParser.ModuloContext ctx) {
		return visit(ctx.links) % visit(ctx.rechts);
	}

	@Override
	public Double visitPower(@NotNull DemoParser.PowerContext ctx) {
		return Math.pow(visit(ctx.links), visit(ctx.rechts));
	}

	@Override
	public Double visitTiny(@NotNull DemoParser.TinyContext ctx) {
		return visit(ctx.links) * Math.pow(10, visit(ctx.rechts));
	}

	@Override
	public Double visitBracket(@NotNull DemoParser.BracketContext ctx) {
		return visit(ctx.expression());
	}

	@Override
	public Double visitVariable(@NotNull DemoParser.VariableContext ctx) {
		if (this.varMap.containsKey(ctx.getText())) {
			return this.varMap.get(ctx.getText()).getValue();
		} else {
			throw new IllegalArgumentException("Unknown Variable: " + ctx.getText() + ". \n");
		}
	}

	@Override
	public Double visitFunctionCall(@NotNull DemoParser.FunctionCallContext ctx) {
		LinkedList<Double> args = new LinkedList<Double>();
		// Extract all those juicy arguments
		for (DemoParser.ExpressionContext c : ctx.expression()) {
			args.add(visit(c));
		}

		if (this.funcMap.containsKey(ctx.name.getText())) {
			// Change the cool Double to the sh*tty c-remnant double
			Double[] t = args.toArray(new Double[args.size()]);
			return this.funcMap.get(ctx.name.getText()).eval(Stream.of(t).mapToDouble(Double::doubleValue).toArray());
		}

		Iterator<Double> it = args.iterator();

		// Predefined Math functions. Because java
		switch (ctx.name.getText()) {
		case "sin":
			return Math.sin(args.getFirst());
		case "cos":
			return Math.cos(args.getFirst());
		case "tan":
			return Math.tan(args.getFirst());
		case "asin":
			return Math.asin(args.getFirst());
		case "acos":
			return Math.acos(args.getFirst());
		case "atan":
			return Math.atan(args.getFirst());
		case "sinh":
			return Math.sinh(args.getFirst());
		case "cosh":
			return Math.cosh(args.getFirst());
		case "tanh":
			return Math.tanh(args.getFirst());
		case "log": // this shouldn't be here, but at ln. well, whatever the profs want i guess
		case "log10":
			return Math.log10(args.getFirst());
		case "ln":
			return Math.log(args.getFirst());
		case "lb":
		case "ld":
		case "log2":
			return Math.log(args.getFirst()) / Math.log(2);
		case "logE":
			return Math.log(args.getFirst()) / Math.log(Math.E);
		case "abs":
			return Math.abs(args.getFirst());
		case "exp":
			return Math.exp(args.getFirst());
		case "sqrt":
			return Math.sqrt(args.getFirst());
		case "min":
			double min = args.getFirst();
			while (it.hasNext()) {
				min = Math.min(min, it.next());
			}
			return min;
		case "max":
			double max = args.getFirst();
			while (it.hasNext()) {
				max = Math.min(max, it.next());
			}
			return max;
		case "pow":
			double pow = args.getFirst();
			while (it.hasNext()) {
				pow = Math.pow(pow, it.next());
			}
			return pow;
		}

		throw new IllegalArgumentException("Unknown Function called: " + ctx.getText());
	}

	@Override
	protected Double aggregateResult(Double aggregate, Double nextResult) {
		if (aggregate == null) {
			return nextResult;
		} else if (nextResult == null) {
			return aggregate;
		}
		return aggregate + nextResult;
	}

	/**
	 * @param varName Name of the Variable to search
	 * @return the Variable with matching name
	 * @throws IllegalArgumentException the searched Variable does not exist
	 */
	public Variable getVariable(String varName) throws IllegalArgumentException {
		/*
		 * Variable v = varList.stream() .filter(w -> varName.equals(w.getName()))
		 * .findAny() .orElse(null); if(v == null) { throw new
		 * IllegalArgumentException("Error 404: Variable not Found"); }
		 */

		if (!this.varMap.containsKey(varName)) {
			throw new IllegalArgumentException("Error 404: Variable '" + varName + "' not found. ");
		} else {
			return this.varMap.get(varName);
		}
		// return v;
	}

	/**
	 * @param varName  the Name of the Variable
	 * @param varValue the Value of the Variable
	 */
	public void setVariable(String varName, double varValue) {
		Variable v;
		try {
			v = getVariable(varName);
			v.setValue(varValue);
		} catch (IllegalArgumentException e) {
			this.varMap.put(varName, new Variable(varName, varValue));
			// this.varList.add(new Variable(varName, varValue));
		}
	}

	/**
	 * @return the current Mapping of Variables and their Names
	 */
	public HashMap<String, Variable> getVarMap() {
		return varMap;
	}

	/**
	 * @param varMap a new map of Variables and their Names
	 */
	public void setVarMap(HashMap<String, Variable> varMap) {
		this.varMap = varMap;
	}

	/**
	 * @return current Mapping of Functions and their Names
	 */
	public HashMap<String, Function> getFuncMap() {
		return funcMap;
	}

	/**
	 * @param funcMap a new map of Functions and their Names
	 */
	public void setFuncMap(HashMap<String, Function> funcMap) {
		this.funcMap = funcMap;
	}

	/**
	 * @return the current Listing of all solutions for this tree
	 */
	public LinkedList<Double> getSolutionList() {
		return solutionList;
	}

}