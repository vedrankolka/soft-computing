package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.controller.Rule;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;

import static hr.fer.zemris.fuzzy.boat.BoatFuzzySets.*;

public class AccelerationController extends FuzzyController {

	public AccelerationController(IBinaryFunction and, IBinaryFunction implication, IBinaryFunction conclusionReductor,
			IDefuzzifier defuzzifier) {
		super(conclusionReductor, defuzzifier, buildRules(and, implication));
	}
	
	private static List<Rule> buildRules(IBinaryFunction and, IBinaryFunction implication) {
		return Arrays.asList(
			// if FAR from both and SLOW then ACCELERATE
			new BoatRule(FAR, FAR, null, null, SLOW, null, ACCELERATE, and, implication),
			// if FAR from both and MODERATELY CLOSE od naprijed and FAST then DECELERATE
			// TODO mozda implementriat OR izmedu dva pa predat tu na oba mjesta ak me kuzis
			// napravim ako je MODERATLY_CLOSE LK ili DK onda nekaj
			new BoatRule(FAR, FAR, MODERATLY_CLOSE, MODERATLY_CLOSE, FAST, null, DECELERATE, and, implication),
			// if CRITICALY CLOSE to both then DECELARATE
			new BoatRule(CRITICALY_CLOSE, CRITICALY_CLOSE, CRITICALY_CLOSE, CRITICALY_CLOSE, null, null, DECELERATE, and, implication),
			// if SLOW then ACCELERATE
			new BoatRule(null, null, null, null, SLOW, null, ACCELERATE, and, implication)
		);
	}
}
