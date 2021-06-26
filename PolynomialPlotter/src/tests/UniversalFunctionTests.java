package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.UniversalFunction;
import model.Koordinate;

import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

public class UniversalFunctionTests {
	
	@Test
	public void calculatesCorrectValues_LinearFunction()
	{
		//Arrange
		UniversalFunction testFunction = new UniversalFunction("2x+1");
		
		double[] xValues = new double[] {
				3.5,
				-999.842,
		};		
		
		Koordinate[] expectedValues = new Koordinate[] { 
				new Koordinate(3.5, 8.0), 
				new Koordinate(-999.842, -1998.684)
				};
		
		//Act
		Koordinate[] returnedValues = new Koordinate[] {
				testFunction.calculate(xValues[0]),
				testFunction.calculate(xValues[1])	
		};		
		
		//Assert
		Assert.assertEquals("x-Value, position "+ 0, expectedValues[0].getX(), returnedValues[0].getX(), 0.0);
		Assert.assertEquals("y-Value, position"+ 0, expectedValues[0].getY(), returnedValues[0].getY(), 0.0);
		
		Assert.assertEquals("x-Value, position "+ 1, expectedValues[1].getX(), returnedValues[1].getX(), 0.0);
		Assert.assertEquals("y-Value, position"+ 1, expectedValues[1].getY(), returnedValues[1].getY(), 0.001);
	}
	
	@Test
	public void calculatesCorrectValues_SquareFunction()
	{
		//Arrange
		UniversalFunction testFunction = new UniversalFunction("2x^3+1");
		
		double[] xValues = new double[] {
				3.5,
				-999.842,
		};		

		Koordinate[] expectedValues = new Koordinate[] { 
				new Koordinate(3.5, 86.75), 
				new Koordinate(-999.842, -1999052149),
				};
		
		//Act
		Koordinate[] returnedValues = new Koordinate[] {
				testFunction.calculate(xValues[0]),
				testFunction.calculate(xValues[1])
		};
		
		//Assert
		Assert.assertEquals("x-Value, position "+ 0, expectedValues[0].getX(), returnedValues[0].getX(), 0.0);
		Assert.assertEquals("y-Value, position"+ 0, expectedValues[0].getY(), returnedValues[0].getY(), 0.0);
		
		Assert.assertEquals("x-Value, position "+ 1, expectedValues[1].getX(), returnedValues[1].getX(), 0.0);
		Assert.assertEquals("y-Value, position"+ 1, expectedValues[1].getY(), returnedValues[1].getY(), 1.0);
	}
//	
//	@Test
//	public void calculate_returnNullInsteadOfException()
//	{
//		UniversalFunction testFunction = new UniversalFunction("yxcv�asdf");
//		Object result = testFunction.calculate(0.0, 10.0, 0.1);
//		
//		Assert.assertEquals(null, result);
//	}
	
	//TODO verhalten wenn 1/0 geteilt wird
}
