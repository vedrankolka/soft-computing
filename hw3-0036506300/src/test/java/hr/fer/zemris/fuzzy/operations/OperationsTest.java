package hr.fer.zemris.fuzzy.operations;

import static org.junit.Assert.*;

import org.junit.Test;

public class OperationsTest {
	
	private static final double EPSILON = 1e-9;

	@Test
	public void hamacherTNormTest() {
		double a = 0.4;
		double b = 0.3;
		IBinaryFunction product = Operations.hamacherTNorm(1.0);
		assertEquals(a*b, product.valueAt(a, b), EPSILON);
		assertEquals(1.0, product.valueAt(1.0, 1.0), EPSILON);
	}
	
}
