package hr.fer.zemris.fuzzy.domain;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CompositeDomain extends Domain {
	
	private SimpleDomain[] components;
	private int cardinality;
	
	public CompositeDomain(SimpleDomain... simpleDomains) {
		assert simpleDomains.length > 1;
		this.components = simpleDomains;
		this.cardinality = 1;
		for (SimpleDomain sd : components)
			this.cardinality *= sd.getCardinality();
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new CompositeDomainIterator();
	}

	@Override
	public int getCardinality() {
		return cardinality;
	}

	@Override
	public IDomain getComponent(int index) {
		return components[index];
	}

	@Override
	public int getNumberOfComponents() {
		return components.length;
	}
	
	@Override
	public DomainElement elementForIndex(int index) {
		int numberOfComponents = getNumberOfComponents();
		DomainElement[] values = new DomainElement[numberOfComponents];
		
		for (int i = numberOfComponents - 1; i > 0; i--) {
			int componentCardinality = getComponent(i).getCardinality();
			int componentIndex = index % componentCardinality;
			values[i] = getComponent(i).elementForIndex(componentIndex);
			index /= componentCardinality;
		}
		
		values[0] = getComponent(0).elementForIndex(index);
		
		return DomainElement.of(values);
	}

	private class CompositeDomainIterator implements Iterator<DomainElement> {

		private DomainElement[] currentElements;
		private Iterator<DomainElement>[] iterators;
		private int counter;
		private int currIndex;
		
		@SuppressWarnings("unchecked")
		public CompositeDomainIterator() {
			int numberOfComponents = getNumberOfComponents();
			this.iterators = new Iterator[numberOfComponents];
			this.currentElements = new DomainElement[numberOfComponents];
			for (int i = 0; i < numberOfComponents; i++) {
				iterators[i] = components[i].iterator();
				if (i < numberOfComponents - 1)
					currentElements[i] = iterators[i].next();
			}
			this.currIndex = numberOfComponents - 1;
		}
		
		@Override
		public boolean hasNext() {
			return counter < getCardinality();
		}

		@Override
		public DomainElement next() {
			if (!hasNext())
				throw new NoSuchElementException();
			// if there are more elements in this component, get the next one
			if (iterators[currIndex].hasNext()) {
				currentElements[currIndex] = iterators[currIndex].next();
			} else {
				// shift the current index until you get to the iterator with an element
				while (currIndex >= 0 && !iterators[currIndex].hasNext())
					currIndex--;
				
				if (currIndex < 0)
					throw new NoSuchElementException();
				// reset all the iterators which have reached their end
				int numberOfComponents = getNumberOfComponents();
				for (int i = currIndex + 1; i < numberOfComponents; i++) {
					iterators[i] = components[i].iterator();
					if (i < numberOfComponents - 1)
						currentElements[i] = iterators[i].next();
				}
				// get the next element of the current iterator
				currentElements[currIndex] = iterators[currIndex].next();
				currIndex = numberOfComponents - 1;
				return next();
			}
			
			counter++;
			return DomainElement.of(currentElements);
		}
	}

}
