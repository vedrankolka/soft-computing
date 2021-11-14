package hr.fer.zemris.fuzzy;

import hr.fer.zemris.fuzzy.domain.Domain;
import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;

public class Demo {
	public static void main(String[] args) {
		IDomain d1 = Domain.intRange(1, 4);
		IDomain d2 = Domain.intRange(6, 9);
		IDomain d3 = Domain.combine(d1, d2);
		IDomain d4 = Domain.combineMultiple(d1, d1, d1);
		print(d1, "{1, 2}");
		print(d2, "{6, 7}");
		print(d3, "{(1, 6), (1, 7), (2, 6), (2, 7)}");
		print(d4, "Sve kombe od 123");
		
		
	}
	
//	public static void main(String[] args) {
//		IDomain d1 = Domain.intRange(1, 2);
//		IDomain d2 = Domain.intRange(-5, 5);
//		IDomain d3 = Domain.intRange(0,  1);
//		IDomain d4 = Domain.intRange(-3, -2);
//		IDomain d5 = Domain.combineMultiple(d1, d2, d3, d4);
//		print(d2, "{-5, -4, -3, -2, -1, 0, 1, 2, 3, 4}");
//		print(d5, "UH");
//	}

	public static void print(IDomain domain, String headingText) {
		if (headingText != null) {
			System.out.println(headingText);
		}
		for (DomainElement e : domain) {
			System.out.println("Element domene: " + e);
		}
		System.out.println("Kardinalitet domene je: " + domain.getCardinality());
		System.out.println();
	}
}
