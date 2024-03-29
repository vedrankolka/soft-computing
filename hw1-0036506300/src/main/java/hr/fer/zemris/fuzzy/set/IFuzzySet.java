package hr.fer.zemris.fuzzy.set;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;

public interface IFuzzySet {
	IDomain getDomain();
	double getValueAt(DomainElement element);
	double getValueAt(int index);
}
