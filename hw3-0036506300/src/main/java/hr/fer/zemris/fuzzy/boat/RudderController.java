package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.boat.set.CompositeFuzzySet;
import hr.fer.zemris.fuzzy.boat.set.IBoatFuzzySet;
import hr.fer.zemris.fuzzy.boat.set.SimpleBoatFuzzySet;
import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;

import static hr.fer.zemris.fuzzy.boat.set.BoatFuzzySets.*;

public class RudderController extends FuzzyController {

	public RudderController(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not, IBinaryFunction implication,
			IBinaryFunction conclusionReductor) {
		super(conclusionReductor, buildRules(and, or, not, implication));
	}
	
	private static List<Rule> buildRules(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not, IBinaryFunction implication) {
		IBoatFuzzySet wrongDirection = new SimpleBoatFuzzySet(WRONG_DIRECTION, S_INDEX);
		IBoatFuzzySet farFromLeft = new SimpleBoatFuzzySet(FAR, LK_INDEX); // TODO izbrisat K
		IBoatFuzzySet farFromRight = new SimpleBoatFuzzySet(FAR, DK_INDEX); // TODO izbrisat K
		return Arrays.asList(
			new Rule(new SimpleBoatFuzzySet(CLOSE, LK_INDEX)
							, HARD_RIGHT, implication
					),
			new Rule(
//					new CompositeFuzzySet(or,
//							new SimpleBoatFuzzySet(FAR, L_INDEX),
//							new SimpleBoatFuzzySet(FAR, LK_INDEX))
						farFromLeft, LEFT, implication
					),
			new Rule(
				//	new CompositeFuzzySet(or,
				//			new SimpleBoatFuzzySet(CLOSE, D_INDEX),
							new SimpleBoatFuzzySet(CLOSE, DK_INDEX)
							, HARD_LEFT, implication
					),
			new Rule(
//					new CompositeFuzzySet(or,
//							new SimpleBoatFuzzySet(FAR, D_INDEX),
//							new SimpleBoatFuzzySet(FAR, DK_INDEX)
							farFromRight, RIGHT, implication
					),
			// if wrong direction and left bank is far, then hard turn left
//			new Rule(
//					new CompositeFuzzySet(and, wrongDirection, farFromLeft), HARD_LEFT, implication
//					),
//			// if wrong direction and right bank is far, then hard turn right
//			new Rule(
//					new CompositeFuzzySet(and, wrongDirection, farFromRight), HARD_RIGHT, implication
//					),
			// if both banks are far, then turn right
			new Rule(wrongDirection, HARD_RIGHT, implication)
//					new CompositeFuzzySet(and,
//							wrongDirection, farFromRight, farFromLeft
//							), HARD_RIGHT, implication
//					)
		);
	}
}
