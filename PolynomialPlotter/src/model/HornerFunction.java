package model;

import java.awt.Color;

public class HornerFunction implements IFunction {
	
	private double[] hornerElements;
	private Color functionColor; 
	
	public HornerFunction(double[] elements)
	{
		hornerElements = elements;
	}

	@Override
	public Koordinate calculate(double xValue) {

		Koordinate koordinate;
		double yValue = hornerElements[hornerElements.length-1];
		
		for (int i = (hornerElements.length-2); i > 0; i--)
		{
			yValue = yValue*xValue + hornerElements[i];
		}
		
		yValue = yValue + hornerElements[0];
		
		koordinate = new Koordinate(xValue, yValue);
		return koordinate;
	}

	@Override
	public void setColor(Color color) {
		functionColor = color;
	}

	@Override
	public Color getColor() {
		return functionColor;
	}

}
