package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.boat.set.CompositeFuzzySet;
import hr.fer.zemris.fuzzy.boat.set.SimpleBoatFuzzySet;
import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;

import static hr.fer.zemris.fuzzy.boat.set.BoatFuzzySets.*;

public class AccelerationController extends FuzzyController {

	public AccelerationController(IBinaryFunction and, IBinaryFunction or, IUnaryFunction not,
			IBinaryFunction implication, IBinaryFunction conclusionReductor,
			IDefuzzifier defuzzifier) {
		super(conclusionReductor, defuzzifier, buildRules(and, or, not, implication));
	}
	
	private static List<NewRule> buildRules(IBinaryFunction and, IBinaryFunction or,  IUnaryFunction not, IBinaryFunction implication) {
		return Arrays.asList(
			// if close to river bank on both parameters, slow down
			new NewRule(
					new CompositeFuzzySet(or,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CRITICALY_CLOSE, L_INDEX),
									new SimpleBoatFuzzySet(CRITICALY_CLOSE, LK_INDEX)
									)
							,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CRITICALY_CLOSE, D_INDEX),
									new SimpleBoatFuzzySet(CRITICALY_CLOSE, DK_INDEX)
									)
							), DECELERATE, implication
					),
			// if close on the side, but not front side, accelerate
			new NewRule(
					new CompositeFuzzySet(or,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CRITICALY_CLOSE, L_INDEX),
									new SimpleBoatFuzzySet(Operations.unaryOperation(CRITICALY_CLOSE, not), LK_INDEX)
									)
							,
							new CompositeFuzzySet(and,
									new SimpleBoatFuzzySet(CRITICALY_CLOSE, D_INDEX),
									new SimpleBoatFuzzySet(Operations.unaryOperation(CRITICALY_CLOSE, not), DK_INDEX)
									)
							), ACCELERATE, implication
					),
			// if critically close to both sides, decelerate
			new NewRule(
					new CompositeFuzzySet(and,
							new SimpleBoatFuzzySet(CRITICALY_CLOSE, L_INDEX),
							new SimpleBoatFuzzySet(CRITICALY_CLOSE, D_INDEX),
							new SimpleBoatFuzzySet(Operations.unaryOperation(VERY_SLOW, not), V_INDEX)
							), DECELERATE, implication
					),
			// if very slow, then accelerate
			new NewRule(
					new SimpleBoatFuzzySet(VERY_SLOW, V_INDEX), ACCELERATE, implication
					),
			// if fast, then decelerate
			new NewRule(
					new SimpleBoatFuzzySet(FAST, V_INDEX), DECELERATE, implication
					),
			new NewRule(
					new CompositeFuzzySet(and,
							new SimpleBoatFuzzySet(WRONG_DIRECTION, S_INDEX),
							new SimpleBoatFuzzySet(Operations.unaryOperation(VERY_SLOW, not), V_INDEX)
							), DECELERATE, implication
					
					)
			);
	}
}
