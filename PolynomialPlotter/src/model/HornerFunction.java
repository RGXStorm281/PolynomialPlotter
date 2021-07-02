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
		double yValue = hornerElements[0];
		
		for (int i = 1; i < hornerElements.length; i++)
		{
			yValue = yValue*xValue + hornerElements[i];
		}
		
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
