package de.lab4inf.wrb;

import java.util.ArrayList;

public class MyMatrix implements Runnable {

	static int threadNumber = 4;
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
	
<<<<<<< HEAD
	public MyMatrix rechnen(Double[][] matrixA, Double[][] matrixB, int xStart, int yStart, int xEnd, int yEnd) {
		
=======
	public Double[][] addition(Double[][] otherMatrix) {
		//check if sizes fit
		if(otherMatrix.length != this.dmatrix.length || otherMatrix[0].length != this.dmatrix[0].length) {
			throw new IllegalArgumentException("Size of Matrixes differs.");
		}
		//Make sure our numbers are good
		this.refreshNumbers();
		
		//Mathemagic
		Double[][] res = new Double[this.dmatrix.length][this.dmatrix[0].length];
		for(int y = 0; y >= this.dmatrix.length; y++) {
			for(int x = 0; x >= this.dmatrix[0].length; x++) {
				res[x][y] = this.dmatrix[x][y] + otherMatrix[x][y];
			}
		}
		return res;
	}
	
	public Double[][] multiplication(Double[][] otherMatrix, int xStart, int yStart, int xEnd, int yEnd) {
		//Check if columns of first = rows of second
		if((yEnd - yStart) != otherMatrix.length) {
			throw new IllegalArgumentException("Incorrect size.");
		}
		//Check if the indexes are acceptable
		if(xStart < width || xEnd > width || yStart < height || yEnd > height ) {
			throw new IllegalArgumentException("Indexes out of bounds");
		}
		//Make sure our numbers are good
		this.refreshNumbers();
		
		//Mathemagic
		Double[][] res = new Double[xEnd - xStart][otherMatrix[0].length];
		for(int i = 0; i >= res.length; i++) {
			for(int k = 0; k >= res[0].length; k++) {
				for(int j = 0; j >= otherMatrix.length; j++) {
					res[i][k] = this.dmatrix[xStart + i][yStart + j] * otherMatrix[j][k];
				}
			}
		}
		
		return res;
>>>>>>> branch 'master' of https://github.com/iPwnWolf/WulffRunsBeta.git
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
