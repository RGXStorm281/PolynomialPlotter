package model;

import net.objecthunter.exp4j.*;

public class UniversalFunction implements IFunction {

	private String functionalTerm;
	
	public UniversalFunction(String term){
		functionalTerm = term;
	}
	
	
	/**
	 * Berechnet die Wertetabelle (y-Werte) für Polynomfunktionen in der allgemeinen Form
	 * @return double array mit den y-Werten
	 */	
	@Override
	public double[] calculate(double start, double end, double step) {
		int valueCounter = (int) ((end - start) / step);
		double[] tableOfValues = new double[valueCounter+1];
		
		for(Integer i = 0; i <= valueCounter; i++) {
			functionalTerm.replace("x", "("+((Integer)((int)(start+(step*i)))).toString()+")");
			Expression functionExpression = new ExpressionBuilder(functionalTerm).build();
			tableOfValues[i] = functionExpression.evaluate();
		}
		
		return tableOfValues;
	}

	@Override
	public String toString(){
		return functionalTerm;
	}
}
