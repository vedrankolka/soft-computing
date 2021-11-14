package hr.fer.zemris.fuzzy.defuzzifier;

import hr.fer.zemris.fuzzy.set.IFuzzySet;

public interface IDefuzzifier {

	int defuzzify(IFuzzySet e);
	
}
