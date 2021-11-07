package hr.fer.zemris.fuzzy;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public class Debug {
	
	public static void print(IDomain domain, String headingText) {
		if (headingText != null)
			System.out.println(headingText);
		
		for (DomainElement e : domain)
			System.out.println("Element domene: " + e);

		System.out.println("Kardinalitet domene je: " + domain.getCardinality());
		System.out.println();
	}
	
	public static void print(IFuzzySet set, String headingText) {
		if (headingText != null)
			System.out.println(headingText);
		
		for (DomainElement e : set.getDomain())
			System.out.format("d(%s)=%.6f%n", e.toString(), set.getValueAt(e));
			// System.out.println("d(" + e + ")=" + set.getValueAt(e));
	}
}
