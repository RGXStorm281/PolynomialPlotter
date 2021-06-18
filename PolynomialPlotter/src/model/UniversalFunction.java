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
	public Koordinate[] calculate(double start, double end, double step) {
		int valueCounter = (int) ((end - start) / step);
		Koordinate[] tableOfValues = new Koordinate[valueCounter+1];
		
		try {
			for(Integer i = 0; i <= valueCounter; i++) {
	                    double currentX = start + (step*i);
	                    String temp = functionalTerm.replace("x", "("+ currentX +")");
	                    Expression functionExpression = new ExpressionBuilder(temp).build();
	                    tableOfValues[i] = new Koordinate(currentX,functionExpression.evaluate());
			}
			
			return tableOfValues;
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
