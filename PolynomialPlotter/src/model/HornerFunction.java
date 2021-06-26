package model;

import java.awt.Point;

public class HornerFunction implements IFunction {
	
	double[] hornerElements;
	
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

}
