package de.lab4inf.wrb;


public class MatrixVisitor extends DemoBaseVisitor<Boolean> {

	public Boolean visitExpression(DemoParser.ExpressionContext ctx) {
		return visitChildren(ctx);
	}
	
	public Boolean visitVariable(DemoParser.VariableContext ctx) {
		return true;
	}
	
	public Boolean visitFunctionCall(DemoParser.FunctionCallContext ctx) {
		return true;
	}
	
	@Override
	protected Boolean aggregateResult(Boolean aggregate, Boolean nextResult) {
		return aggregate || nextResult;
	} 

}
