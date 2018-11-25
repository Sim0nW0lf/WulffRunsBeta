package de.lab4inf.wrb;

import java.util.HashMap;
import java.util.Iterator;
//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Map;
import java.util.stream.Stream;

//import org.antlr.v4.parse.ANTLRParser;
//import org.antlr.v4.runtime.misc.NotNull;
//import org.antlr.v4.runtime.ParserRuleContext;
//import org.antlr.v4.runtime.tree.ParseTree;


import de.lab4inf.wrb.DemoBaseVisitor;
//import de.lab4inf.wrb.DemoParser.ExpressionContext;

public class MyVisitor extends DemoBaseVisitor<Double> {

	// double solution;
	LinkedList<Double> solutionList = new LinkedList<Double>();
	// LinkedList<Variable> varList = new LinkedList<Variable>();
	HashMap<String, Variable> varMap = new HashMap<String, Variable>();
	HashMap<String, Function> funcMap = new HashMap<String, Function>();
	HashMap<String, MyMatrix> matrixMap = new HashMap<String, MyMatrix>();

	HashMap<String, Double[][]> matrixSolutionsMap = new HashMap<String, Double[][]>();

	Double[][] solutionMatrix;


	/**
	 * @return the last Entry to solutionList, the most recent solved equation.
	 */
	public double getErgebnis() {
		return solutionList.getLast();
	}
	
	public Double[][] getMatrixErgebnis() {
		return solutionMatrix;
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
	public Double visitDivision(DemoParser.DivisionContext ctx) {
		return visit(ctx.links) / visit(ctx.rechts);
	}

	@Override
	public Double visitMultiplikation(DemoParser.MultiplikationContext ctx) {
		return visit(ctx.links) * visit(ctx.rechts);
	}

	@Override
	public Double visitSubtraktion(DemoParser.SubtraktionContext ctx) {
		return visit(ctx.links) - visit(ctx.rechts);
	}

	@Override
	public Double visitAddition(DemoParser.AdditionContext ctx) {
		return visit(ctx.links) + visit(ctx.rechts);
	}

	@Override
	public Double visitNumber(DemoParser.NumberContext ctx) {
		Double d = Double.parseDouble(ctx.NUMBER().getText()); 
		if (ctx.getChildCount() > 1 && ctx.sign.getType() == DemoParser.SUB) {
			d *= -1;
		}
		return d;
	}

	@Override
	public Double visitModulo(DemoParser.ModuloContext ctx) {
		return visit(ctx.links) % visit(ctx.rechts);
	}

	@Override
	public Double visitPower(DemoParser.PowerContext ctx) {
		return Math.pow(visit(ctx.links), visit(ctx.rechts));
	}

	@Override
	public Double visitTiny(DemoParser.TinyContext ctx) {
		return visit(ctx.links) * Math.pow(10, visit(ctx.rechts));
	}

	@Override
	public Double visitBracket(DemoParser.BracketContext ctx) {
		if (ctx.getChildCount() > 3 && ctx.sign.getType() == DemoParser.SUB) {
			return visit(ctx.expression()) * -1;
		}
		return visit(ctx.expression());
	}

	@Override
	public Double visitVariable(DemoParser.VariableContext ctx) {
		if (this.varMap.containsKey(ctx.getText())) {
			return this.varMap.get(ctx.getText()).getValue();
		} else {
			throw new IllegalArgumentException("Unknown Variable: " + ctx.getText() + ". \n");
		}
	}

	@Override
	public Double visitFunctionCall(DemoParser.FunctionCallContext ctx) {
		LinkedList<Double> args = new LinkedList<Double>();
		// Extract all those juicy arguments
		for (DemoParser.ExpressionContext c : ctx.expression()) {
			args.add(visit(c));
		}

		if (this.funcMap.containsKey(ctx.name.getText())) {
			// Change the cool Double to the shitty c-remnant double
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
			double min = it.next();
			while (it.hasNext()) {
				min = Math.min(min, it.next());
			}
			return min;
		case "max":
			double max = it.next();
			while (it.hasNext()) {
				max = Math.max(max, it.next());
			}
			return max;
		case "pow":
			double pow = it.next();
			while (it.hasNext()) {
				pow = Math.pow(pow, it.next());
			}
			return pow;
		}

		throw new IllegalArgumentException("Unknown Function called: " + ctx.getText());
	}
	
	@Override
	public Double visitMatrixDefinition(DemoParser.MatrixDefinitionContext ctx) {
		MyMatrix m = new MyMatrix(this, ctx, ctx.matrixRow().size(), ctx.matrixRow(0).expression().size());
		this.matrixMap.put(ctx.name.getText(), m);
		
		MatrixVisitor mV = new MatrixVisitor();
		
		int[] i = {0, 0};
		for(DemoParser.MatrixRowContext c : ctx.matrixRow()) {
			for(DemoParser.ExpressionContext d : c.expression()) {
				if(mV.visitExpression(d)) {
					m.getVarFields().add(i);
				} else {
					try {
						m.getDmatrix()[i[0]][i[1]] = visit(d);
					} catch (Exception e) {
						throw new IllegalArgumentException("Bad Matrix definition");
					}
				}
				i[1]++;
			}
			i[0]++;
		}
		
		
		return 0.0;
	}
	
	// I am useless sry, dont get this shit :P
	@Override
	public Double visitMatrixMultiplikation(DemoParser.MatrixMultiplikationContext ctx) {
		MyMatrix m; // initialisieren? Hab ich multi falsch aufgerufen? KP
		solutionMatrix = m.multiplication(matrixMap.get(ctx.links.name.getText()).dmatrix, 0, 0, 2, 2); // just trying out
		return 0.0;
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
	 * @return current Mapping of Functions and their Names
	 */
	public HashMap<String, Function> getFuncMap() {
		return funcMap;
	}
}