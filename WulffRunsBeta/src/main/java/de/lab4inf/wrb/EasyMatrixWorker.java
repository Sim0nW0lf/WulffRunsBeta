package de.lab4inf.wrb;

public class EasyMatrixWorker implements Runnable {

	int index = 0;
	protected MyMatrix matrixA;
	protected MyMatrix matrixB;
	volatile protected Double[][] solutionMatrix;
	
	
	public EasyMatrixWorker(MyMatrix matrixA, MyMatrix matrixB, Double[][] solutionMatrix, int index) {
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.solutionMatrix = solutionMatrix;
		this.index = index;
	}
	
	@Override
	public void run() {
			matrixA.multiplyParallelAndSeriell(matrixB, solutionMatrix, index, index+1);
	}
}
