package hr.fer.zemris.fuzzy.set;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;

public class MutableFuzzySet extends FuzzySet {

	private double[] memberships;
	
	public MutableFuzzySet(IDomain domain, double[] memberships) {
		super(domain);
		this.memberships = memberships;
	}
	
	public MutableFuzzySet(IDomain domain) {
		this(domain, new double[domain.getCardinality()]);
	}

	@Override
	public double getValueAt(DomainElement element) {
		return memberships[domain.indexOfElement(element)];
	}
	
	public MutableFuzzySet set(DomainElement element, double value) {
		memberships[domain.indexOfElement(element)] = value;
		return this;
	}
	
	public MutableFuzzySet set(int index, double value) {
		memberships[index] = value;
		return this;
	}

	@Override
	public double getValueAt(int index) {
		return memberships[index];
	}

}
