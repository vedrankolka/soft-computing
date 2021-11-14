package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.boat.set.CompositeFuzzySet;
import hr.fer.zemris.fuzzy.boat.set.SimpleBoatFuzzySet;
import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;

import static hr.fer.zemris.fuzzy.boat.set.BoatFuzzySets.*;

public class RudderController extends FuzzyController {

	public RudderController(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not, IBinaryFunction implication,
			IBinaryFunction conclusionReductor, IDefuzzifier defuzzifier) {
		super(conclusionReductor, defuzzifier, buildRules(and, or, not, implication));
	}
	
	private static List<NewRule> buildRules(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not, IBinaryFunction implication) {
		return Arrays.asList(
			new NewRule(
					new CompositeFuzzySet(or,
							new SimpleBoatFuzzySet(CRITICALY_CLOSE, L_INDEX),
							new SimpleBoatFuzzySet(CRITICALY_CLOSE, LK_INDEX)
							), HARD_RIGHT, implication
					),
			new NewRule(
					new CompositeFuzzySet(or,
							new SimpleBoatFuzzySet(MODERATLY_CLOSE, L_INDEX),
							new SimpleBoatFuzzySet(MODERATLY_CLOSE, LK_INDEX)
							), RIGHT, implication
					),
			new NewRule(
					new CompositeFuzzySet(or,
							new SimpleBoatFuzzySet(CRITICALY_CLOSE, D_INDEX),
							new SimpleBoatFuzzySet(CRITICALY_CLOSE, DK_INDEX)
							), HARD_LEFT, implication
					),
			new NewRule(
					new CompositeFuzzySet(or,
							new SimpleBoatFuzzySet(MODERATLY_CLOSE, D_INDEX),
							new SimpleBoatFuzzySet(MODERATLY_CLOSE, DK_INDEX)
							), LEFT, implication
					),
			// if wrong direction and left bank is far, then hard turn left
			new NewRule(
					new CompositeFuzzySet(and,
							new SimpleBoatFuzzySet(WRONG_DIRECTION, S_INDEX),
							new SimpleBoatFuzzySet(FAR, L_INDEX)
							), HARD_LEFT, implication
					),
			new NewRule(
					new CompositeFuzzySet(and,
							new SimpleBoatFuzzySet(WRONG_DIRECTION, S_INDEX),
							new SimpleBoatFuzzySet(FAR, D_INDEX)
							), HARD_RIGHT, implication
					)
		);
	}
}
