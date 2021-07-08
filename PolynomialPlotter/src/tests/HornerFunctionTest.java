package tests;

import org.junit.Assert;
import org.junit.Test;

import model.HornerFunction;
import model.Koordinate;


public class HornerFunctionTest {
	
	@Test
	public void calculatesCorrectValues_LinearFunction()
	{
		//Arrange
		double[] testElements = new double[] {
				4.8,
				-6.52,
				22.04,
				-12.4,
				80.4,
				-75.04,
				295.872,
				-31.088,
				-199.392
		};	
		
		HornerFunction testFunction = new HornerFunction(testElements);
		
		double[] xValues = new double[] {
				3.5,
				-4.83,
		};	
		
		Koordinate[] expectedValues = new Koordinate[] {
				new Koordinate(3.5, -1192514.608),
				new Koordinate(-4.83, 11004746.07)
		};		
		
		//Act
		Koordinate[] returnedValues = new Koordinate[] {
				testFunction.calculate(xValues[0]),
				testFunction.calculate(xValues[1])
		};
		
		//Assert
		Assert.assertEquals("x-Value, position "+ 0, expectedValues[0].getX(), returnedValues[0].getX(), 0.0);
		Assert.assertEquals("y-Value, position"+ 0, expectedValues[0].getY(), returnedValues[0].getY(), 0.001);
				
		Assert.assertEquals("x-Value, position "+ 1, expectedValues[1].getX(), returnedValues[1].getX(), 0.0);
		Assert.assertEquals("y-Value, position"+ 1, expectedValues[1].getY(), returnedValues[1].getY(), 0.01);
	}

}
