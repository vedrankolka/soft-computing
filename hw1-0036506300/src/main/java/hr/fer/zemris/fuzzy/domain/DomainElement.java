package hr.fer.zemris.fuzzy.domain;

import java.util.Arrays;

/**
 * 
 * 
 * @author gudi
 *
 */
public class DomainElement {

	private int[] values;

	public DomainElement(int... values) {
		super();
		this.values = values;
	}

	public int getNumberOfComponents() {
		return values.length;
	}

	public int getComponentValue(int index) {
		return values[index];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(values);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomainElement other = (DomainElement) obj;
		if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (getNumberOfComponents() == 1)
			return String.valueOf(values[0]);
		
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
			if (i < values.length - 1)
				sb.append(',').append(' ');
		}
		return sb.append(')').toString();
	}

	public static DomainElement of(int... values) {
		return new DomainElement(values);
	}
	
	public static DomainElement of(DomainElement... components) {
		int[] values = new int[components.length];
		for (int i = 0; i < values.length; i++)
			values[i] = components[i].getComponentValue(0);
		return new DomainElement(values);
	}

}
