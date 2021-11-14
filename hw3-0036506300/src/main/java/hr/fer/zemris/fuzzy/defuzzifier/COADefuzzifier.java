package hr.fer.zemris.fuzzy.defuzzifier;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.set.IFuzzySet;

public class COADefuzzifier implements IDefuzzifier {

    @Override
    public int defuzzify(IFuzzySet set) {
    	// Xcoa = sum(miA(xi) * xi) / sum(miA(xi))
        if (set.getDomain().getNumberOfComponents() > 1)
        	throw new IllegalArgumentException();
        
    	double nominator = 0.0;
    	double denominator = 0.0;
    	int i = 0;
    	for (DomainElement e : set.getDomain()) {
    		double mi = set.getValueAt(i++);
    		nominator += e.getComponentValue(0) * mi;
    		denominator += mi;
    	}
    	return (int) Math.round(nominator / denominator);
    }

}
