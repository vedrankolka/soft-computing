package hr.fer.zemris.fuzzy.set;
/**
 * The class offers a selection of standard membership functions.
 * 
 * @author gudi
 *
 */
public class StandardFuzzySets {

	public static IIntUnaryFunction gammaFunction(int a, int b) {
		assert a < b;
		return x -> {
			if (x < a) return 0;
			if (x < b) return (x - a) / (double) (b - a);
			else return 1;
		};
	}
	
	public static IIntUnaryFunction lFunction(int a, int b) {
		assert a < b;
		return x -> {
			if (x < a) return 1;
			if (x < b) return (b - x) / (double) (b - a);
			else return 0;
		};
	}
	
	public static IIntUnaryFunction lambdaFunction(int a, int b, int c) {
		assert a < b;
		assert b < c;
		return x -> {
			if (x < a) return 0;
			if (x < b) return (x - a) / (double) (b - a);
			if (x < c) return (c - x) / (double) (c - b);
			else return 0;
		};
	}
	
	public static IIntUnaryFunction piFunction(int a, int b, int c, int d) {
		assert a < b;
		assert b < c;
		assert c < d;
		return x -> {
			if (x < a) return 0;
			if (x < b) return (x - a) / (double) (b - a);
			if (x < c) return 1;
			if (x < d) return (d - x) / (double) (d - c);
			else return 0;
		};
	}
	
}
