package hr.fer.zemris.fuzzy.boat;

import java.util.Scanner;

import hr.fer.zemris.fuzzy.controller.IFuzzyController;
import hr.fer.zemris.fuzzy.defuzzifier.COADefuzzifier;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public class BoatController implements IFuzzyController {

	public static final String STOP_SEQUENCE = "KRAJ";
	public static final String DELIMITER = "\\s+";

	private IFuzzyController accelerationController;
	private IFuzzyController rudderController;
	private IFuzzySet[] outputs = new IFuzzySet[2];

	public BoatController(IFuzzyController accelerationController, IFuzzyController rudderController) {
		this.accelerationController = accelerationController;
		this.rudderController = rudderController;
	}

	@Override
	public IFuzzySet[] getOutputs(int... inputs) {
		outputs[0] = accelerationController.getOutputs(inputs)[0];
		outputs[1] = rudderController.getOutputs(inputs)[0];
		return outputs;
	}
	
	public static void main(String[] args) {
		
		IBinaryFunction mamdaniImplication = Operations.zadehAnd();
		IBinaryFunction and = Operations.hamacherTNorm(1.0);
		IBinaryFunction or = Operations.zadehOr();
		IUnaryFunction not = Operations.zadehNot();
		
		IDefuzzifier defuzzifier = new COADefuzzifier();
		IFuzzyController accelerationController = new AccelerationController(and, or, not, mamdaniImplication, or);
		IFuzzyController rudderController = new RudderController(and, or, not, mamdaniImplication, or);
		IFuzzyController boatController = new BoatController(accelerationController, rudderController);
		
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				String line = sc.nextLine();
				if (STOP_SEQUENCE.equals(line))
					break;
				
				String[] lineParts = line.split(DELIMITER);
				int l = Integer.parseInt(lineParts[0]);
				int d = Integer.parseInt(lineParts[1]);
				int lk = Integer.parseInt(lineParts[2]);
				int dk = Integer.parseInt(lineParts[3]);
				int v = Integer.parseInt(lineParts[4]);
				int s = Integer.parseInt(lineParts[5]);
				
				IFuzzySet[] fuzzyOutputs = boatController.getOutputs(l, d, lk, dk, v, s);
				System.out.println(defuzzifier.defuzzify(fuzzyOutputs[0]) + " " + defuzzifier.defuzzify(fuzzyOutputs[1]));
				System.out.flush();
			}
		}

	}
}
