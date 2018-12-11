package de.lab4inf.wrb;

import java.util.ArrayList;

import de.lab4inf.wrb.DemoParser.ExpressionContext;

public class MyMatrix {

	static int threadNumber = 64;
	protected DemoParser.ExpressionContext[][] matrix;
	protected double[][] dmatrix;
	protected ArrayList<int[]> varFields = new ArrayList<int[]>();
	protected DemoParser.MatrixDefinitionContext matrixRoot;
	protected MyVisitor parent;
	protected int height;
	protected int width;

	public MyMatrix(MyVisitor parent, DemoParser.MatrixDefinitionContext matrix, int height, int width) {
		this.parent = parent;
		this.matrixRoot = matrix;
		this.height = height;
		this.width = width;
		this.dmatrix = new double[height][width];
		this.matrix = new ExpressionContext[this.dmatrix.length][this.dmatrix[0].length];
	}
	
	public MyMatrix(double[][] matrix) {
		this.dmatrix = matrix;
		this.height = matrix.length;
		this.width = matrix[0].length;
	}

	public void refreshNumbers() {
		for (int[] i : this.varFields) {
			ExpressionContext test = this.matrix[i[0]][i[1]];
			this.dmatrix[i[0]][i[1]] = this.parent.visit(test);
		}
	}
	
	// Divide and Conquer
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//TODO: Divide and Conquer funktioniert nur für Matritzen mit NxN Feldern!
	
	public double[][] matAddition(double[][] matrixA, double[][] matrixB) {
		// check if sizes fit
		if (matrixB.length != matrixA.length || matrixB[0].length != matrixA[0].length) {
			throw new IllegalArgumentException("Size of Matrixes differs.");
		}
		
		// Mathemagic
		double[][] res = new double[matrixA.length][matrixA[0].length];
		for (int y = 0; y < matrixA.length; y++) {
			for (int x = 0; x < matrixA[0].length; x++) {
				res[y][x] = matrixA[y][x] + matrixB[y][x];
			}
		}
		return res;
	}
	
	private void matrixSplitByIndex(double[][] matrix, double[][] M1,double[][] M2, double[][] M3, double[][] M4, int middleY, int middleX) {
		for(int y = 0; y < M1.length; y++) {
			for(int x = 0; x < M1[0].length; x++) {
				M1[y][x] = matrix[y][x];
			}
		}
		for(int y = 0; y < M2.length; y++) {
			for(int x = M1[0].length; x < matrix[0].length; x++) {
				M2[y][x-M1[0].length] = matrix[y][x];
			}
		}
		for(int y = M2.length; y < matrix.length; y++) {
			for(int x = 0; x < M3[0].length; x++) {
				M3[y-M2.length][x] = matrix[y][x];
			}
		}
		for(int y = M2.length; y < matrix.length; y++) {
			for(int x = M3[0].length; x < matrix[0].length; x++) {
				M4[y-M2.length][x-M3[0].length] = matrix[y][x];
			}
		}
	}
	
	public double[][] dAndCMultiTranspose(double[][] matrixA, double[][] matrixB) {
//		 Check if rows of first = columns of second
		if (matrixA[0].length != matrixB.length) {
			throw new IllegalArgumentException("Incorrect size.");
		}
		int bWidth = matrixB[0].length, bHeight = matrixB.length, aHeight = matrixA.length;
		
		double[][] res = new double[aHeight][bWidth], transMatrixB = transposeMatrix(matrixB);

		// Mathemagic
		for (int i = 0; i < aHeight; i++) {
			for (int j = 0; j < bWidth; j++) {
				res[i][j] = 0.0;
				for (int k = 0; k < bHeight; k++) {
					res[i][j] += matrixA[i][k] * transMatrixB[j][k];
				}
			}
		}
		
		return res;
	}
	
	public void matrixMerge(double[][] M1, double[][] M2, double[][] M3, double[][] M4, double[][] solutionMatrix){
		for(int y = 0; y < M1.length; y++) {
			for(int x = 0; x < M1[0].length; x++) {
				solutionMatrix[y][x] = M1[y][x];
			}
		}
		for(int y = 0; y < M2.length; y++) {
			for(int x = M1[0].length; x < solutionMatrix[0].length; x++) {
				solutionMatrix[y][x] = M2[y][x-M1[0].length];
			}
		}
		for(int y = M1.length; y < solutionMatrix.length; y++) {
			for(int x = 0; x < M1[0].length; x++) {
				solutionMatrix[y][x] = M3[y-M1.length][x];
			}
		}
		for(int y = M3.length; y < solutionMatrix.length; y++) {
			for(int x = M1[0].length; x < solutionMatrix[0].length; x++) {
				solutionMatrix[y][x] = M4[y-M3.length][x-M1[0].length];
			}
		}
	}
	
	public MyMatrix matDivideConquer(MyMatrix otherMatrixObjekt) {
		double[][] solutionMatrix = new double[height][otherMatrixObjekt.getWidth()];
		int middleYA = this.dmatrix.length/2, middleXA = this.dmatrix[0].length/2, middleYB = otherMatrixObjekt.height/2, middleXB = otherMatrixObjekt.dmatrix[0].length/2;
		double[][]
		A1 = new double[middleYA][middleXA],
		A2 = new double[middleYA][this.dmatrix[0].length-middleXA],
		A3 = new double[this.dmatrix.length-middleYA][middleXA],
		A4 = new double[this.dmatrix.length-middleYA][this.dmatrix[0].length-middleXA],
		B1 = new double[middleYB][middleXB],
		B2 = new double[middleYB][otherMatrixObjekt.width-middleXB],
		B3 = new double[otherMatrixObjekt.height-middleYB][middleXB],
		B4 = new double[otherMatrixObjekt.height-middleYB][otherMatrixObjekt.width-middleXB],
		C1 = new double[middleYA][middleXB],
		C2 = new double[middleYA][otherMatrixObjekt.dmatrix[0].length-middleXB],
		C3 = new double[this.dmatrix.length-middleYA][otherMatrixObjekt.dmatrix[0].length-middleXB],
		C4 = new double[this.dmatrix.length-middleYA][this.dmatrix[0].length-middleXA];

		// Make sure our numbers are good
		this.refreshNumbers();
		otherMatrixObjekt.refreshNumbers();
		
		matrixSplitByIndex(this.dmatrix, A1, A2, A3, A4, middleYA, middleXA);
		matrixSplitByIndex(otherMatrixObjekt.dmatrix, B1, B2, B3, B4, middleXA, middleXB);
		Thread T1, T2, T3, T4, T5, T6, T7, T8;
		
		MatDAndCMulti W1 = new MatDAndCMulti(A1, B1, this), W2 = new MatDAndCMulti(A2, B3, this), W3 = new MatDAndCMulti(A1, B2, this), W4 = new MatDAndCMulti(A2, B4, this),
					W5 = new MatDAndCMulti(A3, B1, this), W6 = new MatDAndCMulti(A4, B3, this), W7 = new MatDAndCMulti(A3, B2, this), W8 = new MatDAndCMulti(A4, B4, this);
		
		T1 = new Thread(W1);
		T2 = new Thread(W2);
		T3 = new Thread(W3);
		T4 = new Thread(W4);
		T5 = new Thread(W5);
		T6 = new Thread(W6);
		T7 = new Thread(W7);
		T8 = new Thread(W8);
		T1.start();
		T2.start();
		T3.start();
		T4.start();
		T5.start();
		T6.start();
		T7.start();
		T8.start();
//		 Wait for all threads to finish
		Boolean dead = false;
		while (!dead) {
			if (!T1.isAlive() && !T2.isAlive() && !T3.isAlive() && !T4.isAlive() && !T5.isAlive() && !T6.isAlive() && !T7.isAlive() && !T8.isAlive()) {
				dead = true;
			}
		}
		
		C1 = matAddition(W1.getMatrixSolution(), W2.getMatrixSolution());
		C2 = matAddition(W3.getMatrixSolution(), W4.getMatrixSolution());
		C3 = matAddition(W5.getMatrixSolution(), W6.getMatrixSolution());
		C4 = matAddition(W7.getMatrixSolution(), W8.getMatrixSolution());
		
		matrixMerge(C1, C2, C3, C4, solutionMatrix);
		return new MyMatrix(solutionMatrix);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// matParallel
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private double[][] transposeMatrix(double[][] matrix) {
		int width = matrix[0].length, height = matrix.length;
		double[][] transposedMatrix = new double [width][height];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				transposedMatrix[x][y] = matrix[y][x];
			}
		}
		return transposedMatrix;
	}
	
	public MyMatrix matMultiTranspose(MyMatrix otherMatrix) {
//		 Check if columns of first = rows of second
		if (this.getWidth() != otherMatrix.getHeight()) {
			throw new IllegalArgumentException("Incorrect size.");
		}
		
		double[][] otherMatrixTrans = transposeMatrix(otherMatrix.dmatrix);

		// Mathemagic
		double[][] res = new double[this.getHeight()][otherMatrix.getWidth()];
		
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < otherMatrix.getWidth(); j++) {
				res[i][j] = 0.0;
				for (int k = 0; k < otherMatrix.getHeight(); k++) {
					res[i][j] += this.dmatrix[i][k] * otherMatrixTrans[j][k];
				}
			}
		}
		return new MyMatrix(res);
	}

	public MyMatrix multiplication(MyMatrix otherMatrix) {
		// Check if columns of first = rows of second
		if (this.getWidth() != otherMatrix.getHeight()) {
			throw new IllegalArgumentException("Incorrect size.");
		}
		// Make sure our numbers are good
		this.refreshNumbers();
		otherMatrix.refreshNumbers();

		// Mathemagic
		double[][] res = new double[this.getHeight()][otherMatrix.getWidth()];
		
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < otherMatrix.getWidth(); j++) {
				res[i][j] = 0.0;
				for (int k = 0; k < otherMatrix.getHeight(); k++) {
					res[i][j] += this.dmatrix[i][k] * otherMatrix.getDmatrix()[k][j];
				}
			}
		}
		
		return new MyMatrix(res);
	}

	public ArrayList<MyMatrix> getSplitColMatrix(MyMatrix otherMatrix, int pieces) {
		// Mathemagic the size of the individual Pieces
		if(otherMatrix.width < pieces) {
			pieces = otherMatrix.width;
		}
		int[] size = new int[pieces];
		for (int i = 0; i < pieces; i++) {
			size[i] = (otherMatrix.getWidth() - (otherMatrix.getWidth() % pieces)) / pieces;
		}
		size[pieces - 1] += otherMatrix.getWidth() % pieces;

		int globalX = 0;
		ArrayList<MyMatrix> ret = new ArrayList<MyMatrix>();

		// stuff all the stuff into the other stuff
		for (int i = 0; i < pieces; i++) {
			double[][] res = new double[otherMatrix.getHeight()][size[i]];

			for (int x = 0; x < size[i]; x++) {
				for (int y = 0; y < otherMatrix.getHeight(); y++) {
					res[y][x] = otherMatrix.getDmatrix()[y][globalX];
				}
				globalX++;
			}

			ret.add(new MyMatrix(res));
		}
		return ret;
	}
	
	public MyMatrix multiplyParrallel(MyMatrix otherMatrix) {
		double factorOfThreads = 0.05;
		int pieces = (int)Math.round(otherMatrix.dmatrix.length*factorOfThreads);
		if(pieces == 0) pieces = 1;
		return multiplyParrallel(otherMatrix, pieces); // MyMatrix.threadNumber   
	}

	public MyMatrix multiplyParrallel(MyMatrix otherMatrix, int piece) {
		ArrayList<MyMatrix> pieces = getSplitColMatrix(otherMatrix, piece);
		MatrixWorker[] t = new MatrixWorker[piece];
		Thread tr[] = new Thread[piece];
		
		int i = 0;

		// Thread stuffs
		for (MyMatrix p : pieces) {
			t[i] = new MatrixWorker(p, this);
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
		double[][] ret = new double[height][otherMatrix.getWidth()];
		int globalX = 0;

		// Synchronizing
		for (i = 0; i < pieces.size(); i++) {
			for (int x = 0; x < t[i].getMatrixGoal().getWidth(); x++) {
				for (int y = 0; y < height; y++) {
					ret[y][globalX] = t[i].getMatrixGoal().getDmatrix()[y][x];
				}
				globalX++;
			}
		}

		return new MyMatrix(ret);
	}

	static boolean compare(double[][] m1, double[][] m2) {
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

	static String print(double[][] m1) {
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

	public double[][] getDmatrix() {
		return dmatrix;
	}

	public DemoParser.MatrixDefinitionContext getMatrixRoot() {
		return matrixRoot;
	}
}