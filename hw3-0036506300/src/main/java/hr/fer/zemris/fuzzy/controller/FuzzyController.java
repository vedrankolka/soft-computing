package hr.fer.zemris.fuzzy.controller;

import java.util.List;

import hr.fer.zemris.fuzzy.boat.Rule;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public abstract class FuzzyController implements IFuzzyController {

	private IBinaryFunction conclusionReductor;
	private List<Rule> rules;
	private IFuzzySet[] resultCache;
	
	public FuzzyController(IBinaryFunction conclusionReductor, List<Rule> rules) {
		this.conclusionReductor = conclusionReductor;
		this.rules = rules;
		this.resultCache = new IFuzzySet[1];
	}
	
	public Rule getRule(int index) {
		return rules.get(index);
	}

	@Override
	public IFuzzySet[] getOutputs(int... inputs) {
		IFuzzySet conclusion = rules.stream()
				.map(r -> r.valueAt(inputs))
				.reduce((c1, c2) -> Operations.binaryOperation(c1, c2, conclusionReductor))
				.orElseThrow();
		
		resultCache[0] = conclusion;
		return resultCache;
	}
}
