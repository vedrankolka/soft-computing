package hr.fer.zemris.fuzzy.domain;

import java.util.Iterator;

public abstract class Domain implements IDomain {

	public static IDomain intRange(int start, int end) {
		return new SimpleDomain(start, end);
	}

	public static IDomain combine(IDomain domain1, IDomain domain2) {
		if (!(domain1 instanceof SimpleDomain) || !(domain2 instanceof SimpleDomain))
			throw new IllegalArgumentException("You cannot combine combined domains.");
		return new CompositeDomain((SimpleDomain) domain1, (SimpleDomain) domain2);
	}

	public static IDomain combineMultiple(IDomain... domains) {
		SimpleDomain[] simpleDomains = new SimpleDomain[domains.length];
		for (int i = 0; i < domains.length; i++) {
			if (!(domains[i] instanceof SimpleDomain))
				throw new IllegalArgumentException("You cannot combine combined domains.");
			simpleDomains[i] = (SimpleDomain) domains[i];
		}

		return new CompositeDomain(simpleDomains);
	}

	@Override
	public int indexOfElement(DomainElement element) {
		if (element == null)
			return -1;

		int i = 0;
		for (DomainElement e : this) {
			if (e.equals(element))
				return i;
			i++;
		}

		return -1;
	}

	@Override
	public DomainElement elementForIndex(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException(index);

		int i = 0;
		for (DomainElement e : this) {
			if (i++ == index)
				return e;
		}

		throw new IndexOutOfBoundsException(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Domain))
			return false;
		
		Domain other = (Domain) obj;
		if (this.getCardinality() != other.getCardinality())
			return false;
		
		Iterator<DomainElement> myIt = iterator();
		Iterator<DomainElement> otherIt = other.iterator();

		while (myIt.hasNext()) {
			if (!myIt.next().equals(otherIt.next()))
				return false;
		}
		return true;
	}

}
