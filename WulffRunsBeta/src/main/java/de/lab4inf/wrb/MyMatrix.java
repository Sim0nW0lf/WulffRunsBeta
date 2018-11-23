package de.lab4inf.wrb;

import java.util.ArrayList;

public class MyMatrix implements Runnable {

	static final int threadNumber = 4;
	protected DemoParser.ExpressionContext[][] matrix;
	protected Double[][] dmatrix;
	protected ArrayList<int[]> varFields = new ArrayList<int[]>();
	protected DemoParser.MatrixDefinitionContext matrixRoot;
	protected MyVisitor parent;
	protected int height;
	protected int width;
	
	public MyMatrix(MyVisitor parent, DemoParser.MatrixDefinitionContext matrix, int width, int height) {
		this.matrixRoot = matrix;
		this.dmatrix = new Double[width][height];
	}
	
	private void refreshNumbers() {
		for(int[] i : this.varFields) {
			this.dmatrix[i[0]][i[1]] = this.parent.visit(this.matrix[i[0]][i[1]]);
		}
	}
	
	public MyMatrix rechnen(Double[][] matrixA, Double[][] matrixB, int xStart, int yStart, int xEnd, int yEnd) {
		
	}
	
	public MyVisitor getParent() {
		return parent;
	}
	public void setParent(MyVisitor parent) {
		this.parent = parent;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
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
	public ArrayList<int[]> getVarFields() {
		return varFields;
	}

	public void setVarFields(ArrayList<int[]> varFields) {
		this.varFields = varFields;
	}

	public Double[][] getDmatrix() {
		return dmatrix;
	}

	public DemoParser.MatrixDefinitionContext getMatrixRoot() {
		return matrixRoot;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
