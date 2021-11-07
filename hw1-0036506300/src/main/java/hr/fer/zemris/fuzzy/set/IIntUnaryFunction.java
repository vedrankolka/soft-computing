package hr.fer.zemris.fuzzy.set;

public interface IIntUnaryFunction {
	/**
	 * Membership function which takes in the index of the domain element and returns the membership value.
	 * 
	 * @param index - Index of the domain element.
	 * @return membership value
	 */
	double valueAt(int index);
}
