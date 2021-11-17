package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.set.IFuzzySet;

public interface IFuzzyController {

	IFuzzySet[] getOutputs(int ...inputs);
	
}
