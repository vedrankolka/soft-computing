package hr.fer.zemris.fuzzy.boat;

import java.util.Scanner;

import hr.fer.zemris.fuzzy.controller.IFuzzyController;
import hr.fer.zemris.fuzzy.defuzzifier.COADefuzzifier;
import hr.fer.zemris.fuzzy.defuzzifier.IDefuzzifier;
import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;

public class BoatController implements IFuzzyController {

	public static final String STOP_SEQUENCE = "KRAJ";
	public static final String DELIMITER = "\\s+";

	private IFuzzyController accelerationController;
	private IFuzzyController rudderController;
	private int[] outputs = new int[2];

	public BoatController(IFuzzyController accelerationController, IFuzzyController rudderController) {
		this.accelerationController = accelerationController;
		this.rudderController = rudderController;
	}

	@Override
	public int[] getOutputs(int... inputs) {
		outputs[0] = accelerationController.getOutputs(inputs)[0];
		outputs[1] = rudderController.getOutputs(inputs)[0];
		return outputs;
	}
	
	public static void main(String[] args) {
		
		IBinaryFunction mamdaniImplication = Operations.zadehAnd();
		IBinaryFunction and = Operations.hamacherTNorm(1.0);
		IBinaryFunction or = Operations.zadehOr();
		
		IDefuzzifier defuzzifier = new COADefuzzifier();
		IFuzzyController accelerationController = new AccelerationController(and, mamdaniImplication, or, defuzzifier);
		IFuzzyController rudderController = new RudderController(and, or, mamdaniImplication, defuzzifier);
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
				
				int[] outputs = boatController.getOutputs(l, d, lk, dk, v, s);
				System.out.println(outputs[0] + " " + outputs[1]);
				System.out.flush();
			}
		}

	}
}
