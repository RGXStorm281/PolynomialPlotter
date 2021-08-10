package model;

import java.awt.Color;
import java.util.Arrays;

public class HornerFunction extends Function implements IFunction {
	
	private double[] hornerElements;
	private Color functionColor; 
	
	public HornerFunction(double[] elements)
	{
		hornerElements = elements;
	}
        
    /**
     * @inheritDoc 
     */
	@Override
	public Koordinate calculate(double xValue) {
		Koordinate koordinate;
		double yValue = hornerElements[hornerElements.length-1];
		
		for (int i = (hornerElements.length-2); i >= 0; i--)
		{
			yValue = yValue*xValue + hornerElements[i];
		}
		
		koordinate = new Koordinate(xValue, yValue);
		return koordinate;
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
	
	/**
	 * 
	 */
	@Override
	public IFunction getAbleitung() {
		if(hornerElements.length < 2) {
			return new HornerFunction(new double[] {0});
		}
		
		double[] ableitung = new double[hornerElements.length - 1];
		for(int i = hornerElements.length - 1; i > 0; i--) {
			ableitung[i - 1] = i * hornerElements[i];
		}
		
		return new HornerFunction(ableitung);
	}
	
	/**
	 * Gibt das HornerElement als String eines gerundeten Doubles zurück (5 Nachkommastellen).
	 */
	@Override
	public String toString() {
		double[] roundetHornerElements = new double[hornerElements.length];
		for(int i = 0; i < hornerElements.length; i++) {
			roundetHornerElements[i] = (double) Math.round(hornerElements[i] * 100000) / 100000;
		}
		
		return Arrays.toString(roundetHornerElements);
	}
}
