package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;

import hr.fer.zemris.fuzzy.controller.Rule;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public class BoatRule extends Rule {

	public BoatRule(IFuzzySet lSet, IFuzzySet rSet, IFuzzySet lkSet, IFuzzySet dkSet, IFuzzySet vSet, IFuzzySet sSet,
			IFuzzySet consequent, IBinaryFunction and, IBinaryFunction implication) {
		super(Arrays.asList(lSet, rSet, lkSet, dkSet, vSet, sSet), consequent, and, implication);
	}
	
}
