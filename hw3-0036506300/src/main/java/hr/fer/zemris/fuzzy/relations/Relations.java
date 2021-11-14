package hr.fer.zemris.fuzzy.relations;

import hr.fer.zemris.fuzzy.domain.Domain;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class Relations {

	public static final double EPSILON = 1e-6;

	public static boolean isUtimesURelation(IFuzzySet relation) {
		// TODO maybe don't do this check?
		if (relation.getDomain().getNumberOfComponents() != 2)
			return false;

		IDomain u1 = relation.getDomain().getComponent(0);
		IDomain u2 = relation.getDomain().getComponent(1);
		return u1.equals(u2);
	}

	/**
	 * Checks if the given <code>relation</code> is symmetric.
	 * 
	 * @param relation to check
	 * @return <code>true</code> if the relation is symmetric, <code>false</code>
	 *         otherwise.
	 */
	public static boolean isSymmetric(IFuzzySet relation) {
		int n = relation.getDomain().getComponent(0).getCardinality();
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				double thisValue = relation.getValueAt(n * i + j);
				double opositeValue = relation.getValueAt(n * j + i);
				if (Math.abs(thisValue - opositeValue) > EPSILON)
					return false;
			}
		}
		return true;
	}

	public static boolean isReflexive(IFuzzySet relation) {
		int n = relation.getDomain().getComponent(0).getCardinality();
		for (int i = 0; i < n; i++) {
			if (Math.abs(relation.getValueAt(n * i + i) - 1) > EPSILON)
				return false;
		}
		return true;
	}

	public static boolean isMaxMinTransitive(IFuzzySet relation) {
		IFuzzySet tempRelation = compositionOfBinaryRelations(relation, relation);
		return tempRelation.equals(relation);
	}

	public static boolean isFuzzyEquivalence(IFuzzySet relation) {
		return isUtimesURelation(relation) && isReflexive(relation) && isSymmetric(relation)
				&& isMaxMinTransitive(relation);
	}

	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet r1, IFuzzySet r2) {
		int m1 = r1.getDomain().getComponent(1).getCardinality();
		int m2 = r2.getDomain().getComponent(0).getCardinality();

		if (m1 != m2)
			throw new IllegalArgumentException(
					"The cardinalities of inner components must match: (n, m), (m, l) " + m1 + " != " + m2);

		int n = r1.getDomain().getComponent(0).getCardinality();
		int m = m1;
		int l = r2.getDomain().getComponent(1).getCardinality();

		IDomain combinedDomain = Domain.combine(r1.getDomain().getComponent(0), r2.getDomain().getComponent(1));
		MutableFuzzySet result = new MutableFuzzySet(combinedDomain);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < l; k++) {
				double max = 0;
				for (int j = 0; j < m; j++) {
					double r1Value = r1.getValueAt(i * n + j);
					double r2Value = r2.getValueAt(j * m + k);
					max = Math.max(max, Math.min(r1Value, r2Value));
				}
				result.set(i * n + k, max);
			}
		}

		return result;
	}

}
