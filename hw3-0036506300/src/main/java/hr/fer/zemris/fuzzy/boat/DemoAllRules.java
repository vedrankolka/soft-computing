package hr.fer.zemris.fuzzy.boat;

import java.util.Arrays;

import hr.fer.zemris.fuzzy.controller.FuzzyController;
import hr.fer.zemris.fuzzy.defuzzifier.COADefuzzifier;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public class DemoAllRules {

	public static void main(String[] args) {
		if (args.length != 6) {
			System.err.println("The program expects 6 arguments: inputs for the controller.");
			System.exit(1);
		}
		
		IBinaryFunction mamdaniImplication = Operations.zadehAnd();
		IBinaryFunction and = Operations.hamacherTNorm(1.0);
		IBinaryFunction or = Operations.zadehOr();
		IUnaryFunction not = Operations.zadehNot();
		
		IDefuzzifier defuzzifier = new COADefuzzifier();
		//FuzzyController controller = new AccelerationController(and, or, not, mamdaniImplication, or);
		FuzzyController controller = new RudderController(and, or, not, mamdaniImplication, or);
		
		int[] inputs = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
		IFuzzySet fuzzyOutput = controller.getOutputs(inputs)[0];
		System.out.println("fuzzy output: " + fuzzyOutput);
		System.out.println("defuzzifiied output = " + defuzzifier.defuzzify(fuzzyOutput));
	}
	
}
