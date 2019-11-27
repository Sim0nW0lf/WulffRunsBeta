package de.lab4inf.wrb;

public class MatDAndCMulti implements Runnable {

	protected double[][] a;
	protected double[][] b;
	volatile protected double[][] c;
	volatile protected MyMatrix matrixObjekt;
	int colStart, colEnd;
	
	public MatDAndCMulti(double[][] a, double[][] b, MyMatrix matrixObjekt) {
		this.a = a;
		this.b = b;
		this.matrixObjekt = matrixObjekt;
	}
	@Override
	public void run() {
		this. c = matrixObjekt.matSeriell(a, b);
	}
	public double[][] getMatrixSolution() {
		return c;
	}
}