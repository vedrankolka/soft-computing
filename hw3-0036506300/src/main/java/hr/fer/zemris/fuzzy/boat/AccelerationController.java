package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.boat.set.CompositeFuzzySet;
import hr.fer.zemris.fuzzy.boat.set.SimpleBoatFuzzySet;
import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;

import static hr.fer.zemris.fuzzy.boat.set.BoatFuzzySets.*;

public class AccelerationController extends FuzzyController {

	public AccelerationController(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not,
			IBinaryFunction implication, IBinaryFunction conclusionReductor) {
		super(conclusionReductor, buildRules(and, or, not, implication));
	}
	
	private static List<Rule> buildRules(IBinaryFunction and, IBinaryFunction or,  IUnaryFunction not, IBinaryFunction implication) {
		return Arrays.asList(
			// if close to river bank on both parameters, slow down
			new Rule(
					new CompositeFuzzySet(or,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CLOSE, L_INDEX),
									new SimpleBoatFuzzySet(CLOSE, LK_INDEX)
									)
							,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CLOSE, D_INDEX),
									new SimpleBoatFuzzySet(CLOSE, DK_INDEX)
									)
							), DECELERATE, implication
					),
			// if close on the side, but not front side, accelerate
			new Rule(
					new CompositeFuzzySet(or,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CLOSE, L_INDEX),
									new SimpleBoatFuzzySet(FAR, LK_INDEX)
									)
							,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CLOSE, D_INDEX),
									new SimpleBoatFuzzySet(FAR, DK_INDEX)
									)
							), ACCELERATE, implication
					),
			// if critically close to both sides and fast, decelerate
			new Rule(
					new CompositeFuzzySet(and,
							new SimpleBoatFuzzySet(CLOSE, L_INDEX),
							new SimpleBoatFuzzySet(CLOSE, D_INDEX),
							new SimpleBoatFuzzySet(FAST, V_INDEX)
							), DECELERATE, implication
					),
			// if very slow, then accelerate
			new Rule(new SimpleBoatFuzzySet(VERY_SLOW, V_INDEX), ACCELERATE, implication)
			// if fast, then decelerate
//			,new Rule(
//					new SimpleBoatFuzzySet(FAST, V_INDEX), DECELERATE, implication
//					)
//			,new Rule(
//					new CompositeFuzzySet(and,
//							new SimpleBoatFuzzySet(WRONG_DIRECTION, S_INDEX),
//							new SimpleBoatFuzzySet(Operations.unaryOperation(VERY_SLOW, not), V_INDEX)
//							), DECELERATE, implication
//					
//					)
			);
	}
}
