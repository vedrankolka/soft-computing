package hr.fer.zemris.fuzzy.boat;

import hr.fer.zemris.fuzzy.domain.Domain;
import hr.fer.zemris.fuzzy.domain.IDomain;

/**
 * A class with all the domains used for the BoatController.
 * @author gudi
 *
 */
public class BoatDomains {
	// domains of inputs
	public static final IDomain DISTANCE = Domain.intRange(0, 1301);
	public static final IDomain VELOCITY = Domain.intRange(0, 150);
	public static final IDomain DIRECTION = Domain.intRange(0, 2);
	// domains of outputs
	public static final IDomain ACCELERATION = Domain.intRange(-50, 51);
	public static final IDomain ANGLE = Domain.intRange(-90, 91);
}
