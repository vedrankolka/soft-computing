package hr.fer.zemris.fuzzy.boat.set;

import static hr.fer.zemris.fuzzy.boat.BoatDomains.*;
import static hr.fer.zemris.fuzzy.set.StandardFuzzySets.*;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.set.CalculatedFuzzySet;
import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class BoatFuzzySets {

	public static final int L_INDEX = 0;
	public static final int D_INDEX = 1;
	public static final int LK_INDEX = 2;
	public static final int DK_INDEX = 3;
	public static final int V_INDEX = 4;
	public static final int S_INDEX = 5;
	
	// DISTANCES
	public static final IFuzzySet CRITICALY_CLOSE = new CalculatedFuzzySet(DISTANCE, lFunction(30, 35));
	public static final IFuzzySet MODERATLY_CLOSE = new CalculatedFuzzySet(DISTANCE, piFunction(20, 30, 50, 75));
	public static final IFuzzySet FAR = new CalculatedFuzzySet(DISTANCE, gammaFunction(30, 300));

	// VELOCITIES
	public static final IFuzzySet VERY_SLOW = new CalculatedFuzzySet(VELOCITY, lFunction(10, 15));
	public static final IFuzzySet SLOW = new CalculatedFuzzySet(VELOCITY, lambdaFunction(10, 20, 30));
	public static final IFuzzySet FAST = new CalculatedFuzzySet(VELOCITY, gammaFunction(20, 30));
	
	// DIRECTIONS
	public static final IFuzzySet WRONG_DIRECTION = new MutableFuzzySet(DIRECTION, new double[] {1, 0});

	// ACCELERATIONS
	public static final IFuzzySet DECELERATE = new CalculatedFuzzySet(ACCELERATION, lFunction(ioe(ACCELERATION, -20), ioe(ACCELERATION, -10)));
	public static final IFuzzySet ZERO_ACCELERATION = new CalculatedFuzzySet(
			ACCELERATION, lambdaFunction(ioe(ACCELERATION, -10), ioe(ACCELERATION, 0), ioe(ACCELERATION, 10))
			);
	public static final IFuzzySet ACCELERATE = new CalculatedFuzzySet(ACCELERATION, gammaFunction(ioe(ACCELERATION, 20), ioe(ACCELERATION, 30)));

	// RUDDER ANGLES
	public static final IFuzzySet HARD_RIGHT = new CalculatedFuzzySet(ANGLE, lFunction(ioe(ANGLE, -70), ioe(ANGLE, -30)));
	public static final IFuzzySet RIGHT = new CalculatedFuzzySet(ANGLE, lambdaFunction(ioe(ANGLE, -40), ioe(ANGLE, -20), ioe(ANGLE, 0)));
	public static final IFuzzySet STRAIGHT = new CalculatedFuzzySet(ANGLE, lambdaFunction(ioe(ANGLE, -10), ioe(ANGLE, 0), ioe(ANGLE, 10)));
	public static final IFuzzySet LEFT = new CalculatedFuzzySet(ANGLE, lambdaFunction(ioe(ANGLE, 0), ioe(ANGLE, 20), ioe(ANGLE, 40)));
	public static final IFuzzySet HARD_LEFT = new CalculatedFuzzySet(ANGLE, gammaFunction(ioe(ANGLE, 30), ioe(ANGLE, 70)));

	/**
	 * Helper method to get index of element with given values in the given domain.
	 * @param domain
	 * @param value
	 * @return index of element with values in the given domain
	 */
	private static int ioe(IDomain domain, int ...values) {
		return domain.indexOfElement(DomainElement.of(values));
	}
}
