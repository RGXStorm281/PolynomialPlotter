package model;

import net.objecthunter.exp4j.*;

public class UniversalFunction implements IFunction {

	private String functionalTerm;
	
	public UniversalFunction(String term){
		functionalTerm = term;
	}
	
	@Override
	public double[] calculate(double start, double end, double step) {
		int valueCount = (int) ((end - start) * step);
		
		Expression functionExpression = new ExpressionBuilder(functionalTerm).build();
		return null;
	}
}
