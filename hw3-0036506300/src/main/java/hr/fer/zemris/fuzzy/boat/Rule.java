package hr.fer.zemris.fuzzy.boat;

import hr.fer.zemris.fuzzy.boat.set.IBoatFuzzySet;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class Rule {

	private IBoatFuzzySet antecedent;
	private IFuzzySet consequent;
	private IBinaryFunction implication;
	private MutableFuzzySet resultCache;
	
	public Rule(IBoatFuzzySet antecedent, IFuzzySet consequent, IBinaryFunction implication) {
		this.antecedent = antecedent;
		this.consequent = consequent;
		this.implication = implication;
		this.resultCache = new MutableFuzzySet(consequent.getDomain());
	}
	
	public IFuzzySet valueAt(int[] inputs) {
		double mi = antecedent.getValueAt(inputs);
		MutableFuzzySet newSet = resultCache;
		for (int i = 0; i < newSet.getDomain().getCardinality(); i++) {
			newSet.set(i, implication.valueAt(consequent.getValueAt(i), mi));
		}
		return newSet;
	}
}
