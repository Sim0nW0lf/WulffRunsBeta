package de.lab4inf.wrb;

public class MatrixWorker implements Runnable {

	protected Double[][] matrixLocal;
	protected MyMatrix matrixTarget;
	volatile protected Double[][] matrixGoal;
	@Override
	public void run() {
		this.matrixGoal = this.matrixTarget.multiplication(matrixLocal, 0, 0, matrixLocal.length, matrixLocal[0].length);
	}
	
	public Double[][] getMatrixLocal() {
		return matrixLocal;
	}
	public void setMatrixLocal(Double[][] matrixLocal) {
		this.matrixLocal = matrixLocal;
	}
	public MyMatrix getMatrixTarget() {
		return matrixTarget;
	}
	public void setMatrixTarget(MyMatrix matrixTarget) {
		this.matrixTarget = matrixTarget;
	}
	public Double[][] getMatrixGoal() {
		return matrixGoal;
	}
	public void setMatrixGoal(Double[][] matrixGoal) {
		this.matrixGoal = matrixGoal;
	}

	
}
