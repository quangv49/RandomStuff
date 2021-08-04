
public class Vector {
	private double[] vector;
	
	public Vector(double[] vector) {
		setVector(vector);
	}
	
	public static Vector multiply(Matrix matrix, Vector vector) {
		double[] result = new double[matrix.getMatrix().length];
		for (int i = 0; i < result.length; i++)
			result[i] = dot(matrix.getMatrix()[i], vector.getVector());
		return new Vector(result);
	}
	
	private static double dot(double[] v1, double[] v2) {
		if (v1.length != v2.length) throw new IllegalArgumentException();
		double result = 0;
		for (int i = 0; i < v1.length; i++)
			result += v1[i] * v2[i];
		return result;
	}
	
	public static double maxCoord(double[] vector) {
		if (vector.length != 2) throw new IllegalArgumentException();
		return (Math.abs(vector[0]) > Math.abs(vector[1]))?Math.abs(vector[0]):Math.abs(vector[1]);
	}
	
	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
	public double[] getVector() {
		return vector;
	}
}
