package hr.fer.zemris.fuzzy.boat.set;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.operations.IBinaryFunction;

public class CompositeFuzzySet implements IBoatFuzzySet {

	private List<IBoatFuzzySet> sets;
	private IBinaryFunction op;
	
	public CompositeFuzzySet(IBinaryFunction op, List<IBoatFuzzySet> sets) {
		if (sets.size() < 2)
			throw new IllegalArgumentException();
		
		this.sets = sets;
		this.op = op;
	}
	
	public CompositeFuzzySet(IBinaryFunction op, IBoatFuzzySet ...sets) {
		this(op, Arrays.asList(sets));
	}

	@Override
	public double getValueAt(int[] inputs) {
		return sets.stream().mapToDouble(s -> s.getValueAt(inputs)).reduce(op::valueAt).orElseThrow();
	}
	
}
