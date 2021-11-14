package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.controller.Rule;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;

import static hr.fer.zemris.fuzzy.boat.BoatFuzzySets.*;

public class RudderController extends FuzzyController {

	public RudderController(IBinaryFunction and, IBinaryFunction implication, IBinaryFunction conclusionReductor,
			IDefuzzifier defuzzifier) {
		super(conclusionReductor, defuzzifier, buildRules(and, implication));
	}
	
	private static List<Rule> buildRules(IBinaryFunction and, IBinaryFunction implication) {
		return Arrays.asList(
			// if CRITICALY CLOSE to LEFT and FAST then HARD_RIGHT
			new BoatRule(MODERATLY_CLOSE, null, CRITICALY_CLOSE, null, FAST, null, HARD_RIGHT, and, implication),
			// if CRITICALY CLOSE to RIGHT and FAST VELOCITY then HARD_LEFT
			new BoatRule(null, MODERATLY_CLOSE, null, CRITICALY_CLOSE, FAST, null, HARD_LEFT, and, implication),
			// if CLOSE to LEFT then RIGHT
			new BoatRule(MODERATLY_CLOSE, null, MODERATLY_CLOSE, null, null, null, RIGHT, and, implication),
			// if CLOSE to RIGHT then LEFT
			new BoatRule(null, MODERATLY_CLOSE, null, MODERATLY_CLOSE, null, null, LEFT, and, implication)
		);
	}
}
