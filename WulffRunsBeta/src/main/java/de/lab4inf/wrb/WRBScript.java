package de.lab4inf.wrb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class WRBScript implements Script {

	@Override
	public double parse(String definition) {
		CharStream input = new ANTLRInputStream(definition);
		DemoLexer lexer = new DemoLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DemoParser parser = new DemoParser(tokens);
		
		ParseTree tree = parser.expression();
		MyVisitor visitor = new MyVisitor();
		visitor.visit(tree);
		return visitor.getErgebnis();
	}

	@Override
	public double parse(InputStream defStream) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getFunctionNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getVariableNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFunction(String name, Function fct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Function getFunction(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getVariable(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVariable(String name, double value) {
		// TODO Auto-generated method stub
		
	}

}
