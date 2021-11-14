package hr.fer.zemris.fuzzy.boat;

import static hr.fer.zemris.fuzzy.boat.BoatDomains.*;
import static hr.fer.zemris.fuzzy.set.StandardFuzzySets.*;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.set.CalculatedFuzzySet;
import hr.fer.zemris.fuzzy.set.IFuzzySet;
import hr.fer.zemris.fuzzy.set.MutableFuzzySet;

public class BoatFuzzySets {

	// DISTANCES
	public static final IFuzzySet CRITICALY_CLOSE = new CalculatedFuzzySet(DISTANCE, lFunction(30, 200));
	public static final IFuzzySet MODERATLY_CLOSE = new CalculatedFuzzySet(DISTANCE, piFunction(100, 200, 300, 400));
	public static final IFuzzySet FAR = new CalculatedFuzzySet(DISTANCE, gammaFunction(300, 500));

	// VELOCITIES
	public static final IFuzzySet SLOW = new CalculatedFuzzySet(VELOCITY, lFunction(5, 60));
	public static final IFuzzySet FAST = new CalculatedFuzzySet(VELOCITY, gammaFunction(40, 80));
	
	// DIRECTIONS
	public static final IFuzzySet CORRECT_DIRECTION = new MutableFuzzySet(DIRECTION, new double[] {0, 1});

	// ACCELERATIONS
	public static final IFuzzySet DECELERATE = new CalculatedFuzzySet(ACCELERATION, lFunction(ioe(ACCELERATION, -50), ioe(ACCELERATION, -5)));
	public static final IFuzzySet ZERO_ACCELERATION = new CalculatedFuzzySet(
			ACCELERATION, lambdaFunction(ioe(ACCELERATION, -10), ioe(ACCELERATION, 0), ioe(ACCELERATION, 10))
			);
	public static final IFuzzySet ACCELERATE = new CalculatedFuzzySet(ACCELERATION, gammaFunction(ioe(ACCELERATION, 5), ioe(ACCELERATION, 50)));

	// RUDDER ANGLES
	public static final IFuzzySet HARD_RIGHT = new CalculatedFuzzySet(ANGLE, lFunction(ioe(ANGLE, -80), ioe(ANGLE, -45)));
	public static final IFuzzySet RIGHT = new CalculatedFuzzySet(ANGLE, lambdaFunction(ioe(ANGLE, -60), ioe(ANGLE, -30), ioe(ANGLE, 0)));
	public static final IFuzzySet STRAIGHT = new CalculatedFuzzySet(ANGLE, lambdaFunction(ioe(ANGLE, -10), ioe(ANGLE, 0), ioe(ANGLE, 10)));
	public static final IFuzzySet LEFT = new CalculatedFuzzySet(ANGLE, lambdaFunction(ioe(ANGLE, 0), ioe(ANGLE, 30), ioe(ANGLE, 60)));
	public static final IFuzzySet HARD_LEFT = new CalculatedFuzzySet(ANGLE, gammaFunction(ioe(ANGLE, 45), ioe(ANGLE, 80)));

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
