package de.lab4inf.wrb;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class WRBScript implements Script {

	// LinkedList<String> varName = new LinkedList<String>();
	// LinkedList<Double> varValue = new LinkedList<Double>();
	MyVisitor visitor = new MyVisitor();

	@Override
	public double parse(String definition) throws IllegalArgumentException{
		CharStream input = new ANTLRInputStream(definition);
		return parse(input);
	}

	@Override
	public double parse(InputStream defStream) throws IOException {
		CharStream input = new ANTLRInputStream(defStream);
		return parse(input);
	}
	
	public double parse(CharStream input) {
		try {
			DemoLexer lexer = new DemoLexer(input);
			lexer.removeErrorListeners();
			lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			DemoParser parser = new DemoParser(tokens);
			
			ParseTree tree = parser.root();
			visitor.visit(tree);
			return visitor.getErgebnis();
		} catch (Exception e) {
//			System.out.println("Not parsable. ");
			throw new IllegalArgumentException("Not Parsable");
		}
	}

	@Override
	public Set<String> getFunctionNames() {
		return visitor.getFuncMap().keySet();
	}

	@Override
	public Set<String> getVariableNames() {
		return visitor.getVarMap().keySet();
	}

	@Override
	public void setFunction(String name, Function fct) {
		visitor.getFuncMap().put(name, fct);
	}

	@Override
	public Function getFunction(String name) throws IllegalArgumentException {
		if (!visitor.getFuncMap().containsKey(name)) {
			throw new IllegalArgumentException("Error 404: Function '" + name + "' not found.");
		}
		return visitor.getFuncMap().get(name);
	}

	@Override
	public double getVariable(String name) throws IllegalArgumentException {
		return visitor.getVariable(name).getValue();
		// int index = varName.indexOf(name);
		// if (index != -1)
		// return varValue.get(index);
		// throw new IllegalArgumentException("Variable " + name + " not found");
	}

	@Override
	public void setVariable(String name, double value) {
		visitor.setVariable(name, value);
		// if(!varName.contains(name)) {
		// varName.add(name);
		// varValue.add(value);
		// }else {
		// varValue.add(varName.indexOf(name), value);
		// }
	}

}
