package model;

import java.awt.Color;

import net.objecthunter.exp4j.*;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;


public class UniversalFunction extends Function implements IFunction {

	private String functionalTerm;
	private Color functionColor;
	
	public UniversalFunction(String term){
		functionalTerm = term;
	}
	
	
	/**
    * @inheritDoc 
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

    /**
    * @inheritDoc 
    */
	@Override
	public String toString(){
		return functionalTerm;
	}

    /**
    * @inheritDoc 
    */
	@Override
	public void setColor(Color color) {
		functionColor = color;
	}

    /**
    * @inheritDoc 
    */
	@Override
	public Color getColor() {
		return functionColor;
	}
}
