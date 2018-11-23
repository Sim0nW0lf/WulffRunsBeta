package de.lab4inf.wrb;

public class MyMatrix implements Runnable {

	protected DemoParser.ExpressionContext[][] matrix;
	protected DemoParser.MatrixDefinitionContext matrixRoot;
	protected MyVisitor parent;
	protected int length;
	protected int width;
	
	public MyMatrix(DemoParser.MatrixDefinitionContext matrix) {
		this.matrixRoot = matrix;
		
	}
	
	public MyVisitor getParent() {
		return parent;
	}
	public void setParent(MyVisitor parent) {
		this.parent = parent;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public DemoParser.ExpressionContext[][] getMatrix() {
		return matrix;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
