
public class Matrix {
	private double[][] matrix;
	
	public Matrix(double[][] matrix) {
		setMatrix(matrix);
	}
	
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}
	
	public double[][] getMatrix() {
		return matrix;
	}
}
