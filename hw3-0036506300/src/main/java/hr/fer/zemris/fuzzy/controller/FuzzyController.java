package hr.fer.zemris.fuzzy.controller;

import java.util.List;

import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public abstract class FuzzyController implements IFuzzyController {

	private IBinaryFunction conclusionReductor;
	private IDefuzzifier defuzzifier;
	private List<Rule> rules;
	
	public FuzzyController(IBinaryFunction conclusionReductor, IDefuzzifier defuzzifier, List<Rule> rules) {
		this.conclusionReductor = conclusionReductor;
		this.defuzzifier = defuzzifier;
		this.rules = rules;
	}

	@Override
	public int[] getOutputs(int... inputs) {
		IFuzzySet conclusion = rules.stream()
				.map(r -> r.valueAt(inputs))
				.reduce((c1, c2) -> Operations.binaryOperation(c1, c2, conclusionReductor))
				.orElseThrow();
		
		return new int[] {defuzzifier.defuzzify(conclusion)};
	}
}
