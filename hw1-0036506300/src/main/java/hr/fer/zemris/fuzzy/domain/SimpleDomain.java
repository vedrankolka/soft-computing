package hr.fer.zemris.fuzzy.domain;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleDomain extends Domain {
	
	private int first;
	private int last;
	
	public SimpleDomain(int first, int last) {
		assert first < last;
		this.first = first;
		this.last = last;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new SimpleDomainIterator();
	}

	@Override
	public int getCardinality() {
		return last - first;
	}

	@Override
	public IDomain getComponent(int index) {
		if (index != 0)
			throw new IndexOutOfBoundsException(index);
		return this;
	}

	@Override
	public int getNumberOfComponents() {
		return 1;
	}
	
	@Override
	public DomainElement elementForIndex(int index) {
		if (index < 0 || index >= getCardinality())
			throw new IndexOutOfBoundsException(index);
		
		return DomainElement.of(first + index);
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleDomain))
			return false;
		
		SimpleDomain other = (SimpleDomain) obj;
		return this.first == other.first && this.last == other.last;
	}
	
	private class SimpleDomainIterator implements Iterator<DomainElement> {

		private int curr;
		
		public SimpleDomainIterator() {
			curr = first;
		}
		
		@Override
		public boolean hasNext() {
			return curr < last;
		}

		@Override
		public DomainElement next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return new DomainElement(curr++);
		}
		
	}

}
