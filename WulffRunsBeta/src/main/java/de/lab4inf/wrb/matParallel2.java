package de.lab4inf.wrb;

public class matParallel2 implements Runnable {

	protected double[][] a;
	protected double[][] b;
	int i;
	volatile protected double[][] c;
	volatile protected MyMatrix matrixObjekt;
	int colStart, colEnd;
	
	public matParallel2(double[][] res, int i, double[][] a, double[][] b, MyMatrix matrixObjekt) {
		this.c = res;
		this.i = i;
		this.a = a;
		this.b = b;
		this.matrixObjekt = matrixObjekt;
	}
	@Override
	public void run() {
		matrixObjekt.matParallel2Multi(c, i, a, b); //this. c[i] = 
	}
	public double[][] getMatrixSolution() {
		return c;
	}
}