package de.lab4inf.wrb;

public class EasyMatrixWorker implements Runnable {

	int start = 0, ende = 0;
	protected MyMatrix matrixA;
	protected MyMatrix matrixB;
	volatile protected Double[][] solutionMatrix;
	
	
	public EasyMatrixWorker(MyMatrix matrixA, MyMatrix matrixB, Double[][] solutionMatrix, int start, int ende) {
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.solutionMatrix = solutionMatrix;
		this.start = start;
		this.ende = ende;
	}
	
	@Override
	public void run() {
			matrixA.multiplyParallelAndSeriell(matrixB, solutionMatrix,  start, ende);
	}
}