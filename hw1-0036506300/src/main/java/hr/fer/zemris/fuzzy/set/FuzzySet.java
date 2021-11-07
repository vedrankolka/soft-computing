package hr.fer.zemris.fuzzy.set;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;

public abstract class FuzzySet implements IFuzzySet {
	
	public static final double EPSILON = 1e-6;

	protected IDomain domain;
	
	public FuzzySet(IDomain domain) {
		this.domain = domain;
	}

	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof FuzzySet)) return false;
		
		FuzzySet other = (FuzzySet) obj;
		
		if (!this.domain.equals(other.domain)) return false;
		
		for (DomainElement e : this.domain) {
			if (Math.abs(this.getValueAt(e) - other.getValueAt(e)) > EPSILON)
				return false;
		}
		return true;
	}

}
