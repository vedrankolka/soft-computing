package hr.fer.zemris.fuzzy.domain;

import static org.junit.Assert.*;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

//import org.junit.jupiter.api.Test;

public class CompositeDomainTest {

	public static IDomain D1 = Domain.intRange(1, 4);

	@BeforeClass
	public static void initDomains() {

	}

	@Test
	public void twoDomainsIterationTest() {
		IDomain u = Domain.intRange(1, 4);
		IDomain d = Domain.combine(u, u);

		Iterator<DomainElement> it = d.iterator();
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 4; j++) {
				assertEquals(DomainElement.of(i, j), it.next());
			}
		}
	}

	@Test
	public void threeDomainsIterationTest() {
		IDomain d1 = Domain.intRange(0, 3);
		IDomain d2 = Domain.intRange(5, 6);
		IDomain d3 = Domain.intRange(8, 10);
		IDomain d = Domain.combineMultiple(d1, d2, d3);

		assertEquals(6, d.getCardinality());
		Iterator<DomainElement> it = d.iterator();
		assertEquals(DomainElement.of(0, 5, 8), it.next());
		assertEquals(DomainElement.of(0, 5, 9), it.next());
		assertEquals(DomainElement.of(1, 5, 8), it.next());
		assertEquals(DomainElement.of(1, 5, 9), it.next());
		assertEquals(DomainElement.of(2, 5, 8), it.next());
		assertEquals(DomainElement.of(2, 5, 9), it.next());
		assertFalse(it.hasNext());
	}

	@Test
	public void bigDomainsIterationTest() {
		IDomain d1 = Domain.intRange(0, 20);
		IDomain d2 = Domain.intRange(-4, 4);
		IDomain d3 = Domain.intRange(1, 50);
		IDomain d4 = Domain.intRange(100, 200);
		IDomain d = Domain.combineMultiple(d1, d2, d3, d4);

		Iterator<DomainElement> it = d.iterator();
		for (int i = 0; i < 20; i++) {
			for (int j = -4; j < 4; j++) {
				for (int k = 1; k < 50; k++) {
					for (int l = 100; l < 200; l++) {
						assertEquals(DomainElement.of(i, j, k, l), it.next());
					}
				}
			}
		}
		assertFalse(it.hasNext());
	}

	@Test
	public void elementForIndexTwoDomainsBaseTest() {
		IDomain d1 = Domain.intRange(1, 4);
		IDomain d = Domain.combine(d1, d1);
		assertEquals(DomainElement.of(1, 1), d.elementForIndex(0));
		assertEquals(DomainElement.of(1, 2), d.elementForIndex(1));
		assertEquals(DomainElement.of(2, 1), d.elementForIndex(3));
		assertEquals(DomainElement.of(2, 3), d.elementForIndex(5));
		assertEquals(DomainElement.of(3, 2), d.elementForIndex(7));
		assertEquals(DomainElement.of(3, 3), d.elementForIndex(8));
	}

	@Test
	public void elementForIndexTwoDomainsTest() {
		IDomain d1 = Domain.intRange(20, 30);
		IDomain d2 = Domain.intRange(60, 70);
		IDomain d = Domain.combine(d1, d2);

		int index = 0;
		for (int i = 20; i < 30; i++) {
			for (int j = 60; j < 70; j++) {
				assertEquals(DomainElement.of(i, j), d.elementForIndex(index++));
			}
		}
	}

	@Test
	public void elementForIndexThreeDomainsTest() {
		IDomain d1 = Domain.intRange(20, 30);
		IDomain d2 = Domain.intRange(60, 70);
		IDomain d3 = Domain.intRange(0, 100);
		IDomain d4 = Domain.intRange(-20, 60);
		IDomain d = Domain.combineMultiple(d1, d2, d3, d4);

		int index = 0;
		for (int i = 20; i < 30; i++) {
			for (int j = 60; j < 70; j++) {
				for (int k = 0; k < 100; k++) {
					for (int l = -20; l < 60; l++)
						assertEquals(DomainElement.of(i, j, k, l), d.elementForIndex(index++));
				}
			}
		}
	}

}
