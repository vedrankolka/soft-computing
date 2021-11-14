package hr.fer.zemris.fuzzy.controller;

import java.util.List;

import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class Rule {
	
	private List<IFuzzySet> antecedent;
	private IFuzzySet consequent;
	private IBinaryFunction and;
	private IBinaryFunction implication;

	public Rule(List<IFuzzySet> antecedent, IFuzzySet consequent, IBinaryFunction and, IBinaryFunction implication) {
		this.antecedent = antecedent;
		this.consequent = consequent;
		this.and = and;
		this.implication = implication;
	}
	
	public IFuzzySet valueAt(int[] inputs) {
		double mi = 1.0;
		for (int i = 0; i < antecedent.size(); i++) {
			if (antecedent.get(i) != null)
				mi = and.valueAt(mi, antecedent.get(i).getValueAt(inputs[i]));
		}
		return imply(mi);
	}
	
	private IFuzzySet imply(double mi) {
		MutableFuzzySet newSet = new MutableFuzzySet(consequent.getDomain());
		for (int i = 0; i < newSet.getDomain().getCardinality(); i++) {
			newSet.set(i, implication.valueAt(consequent.getValueAt(i), mi));
		}
		return newSet;
	}

}
