package hr.fer.zemris.fuzzy.boat.set;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public class SimpleBoatFuzzySet implements IBoatFuzzySet {

	private IFuzzySet set;
	private int inputIndex;
	
	public SimpleBoatFuzzySet(IFuzzySet set, int inputIndex) {
		this.set = set;
		this.inputIndex = inputIndex;
	}

	@Override
	public double getValueAt(int[] inputs) {
		return set.getValueAt(DomainElement.of(inputs[inputIndex]));
	}

}
