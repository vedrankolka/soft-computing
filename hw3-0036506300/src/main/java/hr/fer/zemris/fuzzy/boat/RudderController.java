package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.boat.set.IBoatFuzzySet;
import hr.fer.zemris.fuzzy.boat.set.SimpleBoatFuzzySet;
import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;

import static hr.fer.zemris.fuzzy.boat.set.BoatFuzzySets.*;

public class RudderController extends FuzzyController {

	public RudderController(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not, IBinaryFunction implication,
			IBinaryFunction conclusionReductor) {
		super(conclusionReductor, buildRules(and, or, not, implication));
	}

	private static List<Rule> buildRules(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not,
			IBinaryFunction implication) {
		IBoatFuzzySet wrongDirection = new SimpleBoatFuzzySet(WRONG_DIRECTION, S_INDEX);
		IBoatFuzzySet farFromLeft = new SimpleBoatFuzzySet(FAR, LK_INDEX); // TODO izbrisat K
		IBoatFuzzySet farFromRight = new SimpleBoatFuzzySet(FAR, DK_INDEX); // TODO izbrisat K
		return Arrays.asList(new Rule(new SimpleBoatFuzzySet(CLOSE, LK_INDEX), HARD_RIGHT, implication),
				new Rule(farFromLeft, LEFT, implication),
				new Rule(new SimpleBoatFuzzySet(CLOSE, DK_INDEX), HARD_LEFT, implication),
				new Rule(farFromRight, RIGHT, implication), new Rule(wrongDirection, HARD_RIGHT, implication));
	}
}
