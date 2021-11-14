package hr.fer.zemris.fuzzy.operations;

import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class Operations {

	private static final IUnaryFunction ZADEH_NOT = x -> 1 - x;
	private static final IBinaryFunction ZADEH_AND = Math::min;
	private static final IBinaryFunction ZADEH_OR = Math::max;

	public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction f) {
		MutableFuzzySet newSet = new MutableFuzzySet(set.getDomain());
		set.getDomain().forEach(e -> newSet.set(e, f.valueAt(set.getValueAt(e))));
		return newSet;
	}

	public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction f) {
		MutableFuzzySet newSet = new MutableFuzzySet(set1.getDomain());
		newSet.getDomain().forEach(e -> newSet.set(e, f.valueAt(set1.getValueAt(e), set2.getValueAt(e))));
		return newSet;
	}

	public static IUnaryFunction zadehNot() {
		return ZADEH_NOT;
	}

	public static IBinaryFunction zadehOr() {
		return ZADEH_OR;
	}

	public static IBinaryFunction zadehAnd() {
		return ZADEH_AND;
	}

	public static IBinaryFunction hamacherTNorm(double v) {
		return (a, b) -> a * b / (v + (1 - v) * (a + b - a * b));
	}

	public static IBinaryFunction hamacherSNorm(double v) {
		return (a, b) -> (a + b - (2 - v) * a * b) / (1 - (1 - v) * a * b);
	}

}
