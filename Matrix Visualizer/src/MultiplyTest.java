import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class MultiplyTest {

	@Before
	public void intialize() {}
	
	@Test
	public void basisTest() {
		Vector b1 = new Vector(new double[] {1,0});
		Vector b2 = new Vector(new double[] {0,1});
		
		Matrix m1 = new Matrix(new double[][] {{1,0},{0,1}});
		Matrix m2 = new Matrix(new double[][] {{3,-7},{5,2}});
		Matrix m3 = new Matrix(new double[][] {{-15.8,1485.3},{89.21,-0.557}});
		
		Vector m1b1 = Vector.multiply(m1,b1);
		Vector m1b2 = Vector.multiply(m1,b2);
		Vector m2b1 = Vector.multiply(m2,b1);
		Vector m2b2 = Vector.multiply(m2,b2);
		Vector m3b1 = Vector.multiply(m3,b1);
		Vector m3b2 = Vector.multiply(m3,b2);
		
		assertTrue(Arrays.equals(m1b1.getVector(), new double[] {1,0}));
		assertTrue(Arrays.equals(m1b2.getVector(), new double[] {0,1}));
		assertTrue(Arrays.equals(m2b1.getVector(), new double[] {3,5}));
		assertTrue(Arrays.equals(m2b2.getVector(), new double[] {-7,2}));
		assertTrue(Arrays.equals(m3b1.getVector(), new double[] {-15.8,89.21}));
		assertTrue(Arrays.equals(m3b2.getVector(), new double[] {1485.3,-0.557}));
	}
	
	@Test
	public void standardTest() {
		Vector v1 = new Vector(new double[] {4,5});
		Vector v2 = new Vector(new double[] {-8,6});
		Vector v3 = new Vector(new double[] {5,0});
		
		Matrix m1 = new Matrix(new double[][] {{6,4},{3,2}});
		Matrix m2 = new Matrix(new double[][] {{5,5},{-9,1}});
		Matrix m3 = new Matrix(new double[][] {{2,-7},{7,-3}});
		
		Vector m1v1 = Vector.multiply(m1,v1);
		Vector m1v2 = Vector.multiply(m1,v2);
		Vector m1v3 = Vector.multiply(m1,v3);
		Vector m2v1 = Vector.multiply(m2,v1);
		Vector m2v2 = Vector.multiply(m2,v2);
		Vector m2v3 = Vector.multiply(m2,v3);
		Vector m3v1 = Vector.multiply(m3,v1);
		Vector m3v2 = Vector.multiply(m3,v2);
		Vector m3v3 = Vector.multiply(m3,v3);
		
		assertTrue(Arrays.equals(m1v1.getVector(), new double[] {44,22}));
		assertTrue(Arrays.equals(m1v2.getVector(), new double[] {-24,-12}));
		assertTrue(Arrays.equals(m1v3.getVector(), new double[] {30,15}));
		assertTrue(Arrays.equals(m2v1.getVector(), new double[] {45,-31}));
		assertTrue(Arrays.equals(m2v2.getVector(), new double[] {-10,78}));
		assertTrue(Arrays.equals(m2v3.getVector(), new double[] {25,-45}));
		assertTrue(Arrays.equals(m3v1.getVector(), new double[] {-27,13}));
		assertTrue(Arrays.equals(m3v2.getVector(), new double[] {-58,-74}));
		assertTrue(Arrays.equals(m3v3.getVector(), new double[] {10,35}));
	}

}
