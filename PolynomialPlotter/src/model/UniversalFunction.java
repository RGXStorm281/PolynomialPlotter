package model;

import net.objecthunter.exp4j.*;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;


public class UniversalFunction implements IFunction {

	private String functionalTerm;
	
	public UniversalFunction(String term){
		functionalTerm = term;
	}
	
	
	/**
	 * Berechnet die Wertetabelle (y-Werte) fuer Polynomfunktionen in der allgemeinen Form
	 * @return Koordinaten-Array mit allen Werten im Zielbereich (x-Koordinate, y-Koordinate)
	 */	
	@Override
	public Koordinate calculate(double xValue) {
		
		try {
			String temp = functionalTerm.replace("x", "("+ xValue +")");
			Expression functionExpression = new ExpressionBuilder(temp).build();
			Koordinate koordinate = new Koordinate(xValue,functionExpression.evaluate());
			return koordinate;
		}
		catch(UnknownFunctionOrVariableException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString(){
		return functionalTerm;
	}
}
