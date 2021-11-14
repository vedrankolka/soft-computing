package hr.fer.zemris.fuzzy.boat;

import hr.fer.zemris.fuzzy.boat.set.IBoatFuzzySet;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class NewRule {

	private IBoatFuzzySet antecedent;
	private IFuzzySet consequent;
	private IBinaryFunction implication;
	// TODO mozda izbacit ovaj cache skroz
	private MutableFuzzySet resultCache;
	
	public NewRule(IBoatFuzzySet antecedent, IFuzzySet consequent, IBinaryFunction implication) {
		this.antecedent = antecedent;
		this.consequent = consequent;
		this.implication = implication;
		this.resultCache = new MutableFuzzySet(consequent.getDomain());
	}
	
	public IFuzzySet valueAt(int[] inputs) {
		double mi = antecedent.getValueAt(inputs);
		// MutableFuzzySet newSet = new MutableFuzzySet(consequent.getDomain());
		MutableFuzzySet newSet = resultCache;
		for (int i = 0; i < newSet.getDomain().getCardinality(); i++) {
			newSet.set(i, implication.valueAt(consequent.getValueAt(i), mi));
		}
		return newSet;
	}
}
