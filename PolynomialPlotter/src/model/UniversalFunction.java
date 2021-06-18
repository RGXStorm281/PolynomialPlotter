package model;

import java.awt.Point;
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
	public Koordinate[] calculate(double start, double end, double step) {
		int valueCounter = (int) ((end - start) / step);
		Koordinate[] tableOfValues = new Koordinate[valueCounter+1];
		
		for(Integer i = 0; i <= valueCounter; i++) {
                    double currentX = start + (step*i);
                    functionalTerm.replace("x", "("+ currentX +")");
                    Expression functionExpression = new ExpressionBuilder(functionalTerm).build();
                    tableOfValues[i] = new Koordinate(currentX,functionExpression.evaluate());
		}
		
		return tableOfValues;
	}

	@Override
	public String toString(){
		return functionalTerm;
	}
}
