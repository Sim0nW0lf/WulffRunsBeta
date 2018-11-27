package de.lab4inf.wrb;

import java.util.ArrayList;

import de.lab4inf.wrb.DemoParser.ExpressionContext;

public class MyMatrix {

	static int threadNumber = 4;
	protected DemoParser.ExpressionContext[][] matrix;
	protected Double[][] dmatrix;
	protected ArrayList<int[]> varFields = new ArrayList<int[]>();
	protected DemoParser.MatrixDefinitionContext matrixRoot;
	protected MyVisitor parent;
	protected int height;
	protected int width;

	public MyMatrix(MyVisitor parent, DemoParser.MatrixDefinitionContext matrix, int width, int height) {
		this.parent = parent;
		this.matrixRoot = matrix;
		this.height = height;
		this.width = width;
		this.dmatrix = new Double[width][height];
		this.matrix = new ExpressionContext[this.dmatrix.length][this.dmatrix[0].length];
	}

	public void refreshNumbers() {
		for (int[] i : this.varFields) {
			ExpressionContext test = this.matrix[i[0]][i[1]];//matrix[i[0]][i[1]];
			this.dmatrix[i[0]][i[1]] = this.parent.visit(test); // this.parent.visit(this.matrix[i[0]][i[1]]);
		}
	}

	public Double[][] addition(Double[][] otherMatrix) {
		// check if sizes fit
		if (otherMatrix.length != this.dmatrix.length || otherMatrix[0].length != this.dmatrix[0].length) {
			throw new IllegalArgumentException("Size of Matrixes differs.");
		}
//		// Make sure our numbers are good
//		this.refreshNumbers();

		// Mathemagic
		Double[][] res = new Double[this.dmatrix.length][this.dmatrix[0].length];
		for (int y = 0; y < this.dmatrix.length; y++) {
			for (int x = 0; x < this.dmatrix[0].length; x++) {
				res[x][y] = this.dmatrix[x][y] + otherMatrix[x][y];
			}
		}
		return res;
	}

	public Double[][] multiplication(Double[][] otherMatrix) {
		return multiplication(otherMatrix, 0, 0, width, height);
	}

	public void multiplyParallelAndSeriell(MyMatrix otherMatrixObjekt, Double[][] solutionMatrix, int yStart, int yEnd) {
		// Make sure our numbers are good
		this.refreshNumbers();
		otherMatrixObjekt.refreshNumbers();
		
		Double[][] otherMatrix = otherMatrixObjekt.dmatrix;
		
		for (int i = yStart; i < yEnd; i++) {
			for (int j = 0; j < solutionMatrix[0].length; j++) {
				// initialize res
				solutionMatrix[i][j] = 0.0;
				for (int k = 0; k < otherMatrix.length; k++) {
					solutionMatrix[i][j] += this.dmatrix[i][k] * otherMatrix[k][j];
				}
			}
		}
		return;
	}
	
	public Double[][] multiplication(Double[][] otherMatrix, int xStart, int yStart, int xEnd, int yEnd) {
		// Check if columns of first = rows of second
		if ((yEnd - yStart) > otherMatrix[0].length) {
			throw new IllegalArgumentException("Incorrect size.");
		}
		// Check if the indexes are acceptable
		if (xStart > width || xEnd > width || yStart > height || yEnd > height) {
			throw new IllegalArgumentException("Indexes out of bounds");
		}
		// Make sure our numbers are good
		this.refreshNumbers();

		// Mathemagic
		Double[][] res = new Double[xEnd - xStart][otherMatrix[0].length];
		for (int x = 0; x < res.length; x++) {
			for (int y = 0; y < res[0].length; y++) {
				res[x][y] = 0.0;
			}
		}
		
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[0].length; j++) {
				for (int k = 0; k < otherMatrix.length; k++) {
					res[i][j] += this.dmatrix[xStart + i][yStart + k] * otherMatrix[k][j];
				}
			}
		}
		
		return res;
	}

	public ArrayList<Double[][]> getSplitColMatrix(Double[][] otherMatrix, int pieces) {
		// Mathemagic the size of the individual Pieces
		if(otherMatrix.length < pieces) {
			pieces = otherMatrix.length;
		}
		int[] size = new int[pieces];
		for (int i = 0; i < pieces; i++) {
			size[i] = otherMatrix.length / pieces;
		}
		size[pieces - 1] += otherMatrix.length % pieces;

		int globalX = 0;
		ArrayList<Double[][]> ret = new ArrayList<Double[][]>();

		// stuff all the stuff into the other stuff
		for (int i = 0; i < pieces; i++) {
			Double[][] res = new Double[otherMatrix[0].length][size[i]];

			for (int x = 0; x < size[i]; x++) {
				for (int y = 0; y < otherMatrix[0].length; y++) {
					res[y][x] = otherMatrix[y][globalX];
				}
				globalX++;
			}

			ret.add(res);
		}
		return ret;
	}
	
	public Double[][] multiplyParrallel(Double[][] otherMatrix) {
		return multiplyParrallel(otherMatrix, MyMatrix.threadNumber);
	}

	public Double[][] multiplyParrallel(Double[][] otherMatrix, int piece) {
		ArrayList<Double[][]> pieces = getSplitColMatrix(otherMatrix, piece);
		MatrixWorker[] t = new MatrixWorker[piece];
		Thread tr[] = new Thread[piece];
		
		int i = 0;

		// Thread stuffs
		for (Double[][] p : pieces) {
			t[i] = new MatrixWorker();
			t[i].setMatrixLocal(p);
			t[i].setMatrixTarget(this);
			tr[i] = new Thread(t[i]);
			tr[i].start();
			// p = multiplication(p);

			i++;
		}

		// Wait for all threads to finish
		i = 0;
		Boolean dead = false;
		while (!dead) {
			if (i >= pieces.size()) {
				dead = true;
			} else {
				if (!tr[i].isAlive()) {
					i++;
				}
			}
		}
		i = 0;
		Double[][] ret = new Double[width][height];
		int globalX = 0;
		
		// Synchronizing
		for (i = 0; i < pieces.size(); i++) {
			for (int x = 0; x < t[i].getMatrixGoal()[0].length; x++) {
				for (int y = 0; y < height; y++) {
					ret[y][globalX] = t[i].getMatrixGoal()[y][x];
				}
			}
			globalX++;
		}

		return ret;
	}

	static boolean compare(Double[][] m1, Double[][] m2) {
		if (m1.length != m2.length || m1[0].length != m2[0].length) {
			return false;
		}
		for (int x = 0; x < m1.length; x++) {
			for (int y = 0; y < m1[0].length; y++) {
				if (m1[x][y] != m2[x][y]) {
					return false;
				}
			}
		}
		return true;
	}

	static String print(Double[][] m1) {
		String s = "";
		for (int x = 0; x < m1.length; x++) {
			s += "[";
			for (int y = 0; y < m1[0].length; y++) {
				s += m1[x][y];
				if (y < m1[0].length - 1) {
					s += ", ";
				}
			}
			s += "]";
		}
		return s;
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
	
	public void addVarField(int y, int x, DemoParser.ExpressionContext ctx) {
		int[] i = {y, x};
		this.varFields.add(i);
		this.matrix[y][x] = ctx;
	}

	public Double[][] getDmatrix() {
		return dmatrix;
	}

	public DemoParser.MatrixDefinitionContext getMatrixRoot() {
		return matrixRoot;
	}

}