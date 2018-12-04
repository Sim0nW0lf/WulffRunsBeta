package de.lab4inf.wrb;

public class MatrixWorker implements Runnable {

	protected MyMatrix matrixLocal;
	protected MyMatrix matrixTarget;
	volatile protected MyMatrix matrixGoal;
	int colStart, colEnd;
	public MatrixWorker(MyMatrix local, MyMatrix target) {
		this.matrixLocal = local;
		this.matrixTarget = target;
	}
	@Override
	public void run() {
		this.matrixGoal = this.matrixTarget.matMultiTranspose(matrixLocal);
	}
	
	public MyMatrix getMatrixLocal() {
		return matrixLocal;
	}
	public void setMatrixLocal(MyMatrix matrixLocal) {
		this.matrixLocal = matrixLocal;
	}
	public MyMatrix getMatrixTarget() {
		return matrixTarget;
	}
	public void setMatrixTarget(MyMatrix matrixTarget) {
		this.matrixTarget = matrixTarget;
	}
	public MyMatrix getMatrixGoal() {
		return matrixGoal;
	}
	public void setMatrixGoal(MyMatrix matrixGoal) {
		this.matrixGoal = matrixGoal;
	}

	
}
