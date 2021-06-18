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
		
		double start = 0.0;
		double end = 5.0;
		double step = 1.0;
		
		Koordinate[] expectedValues = new Koordinate[] { 
				new Koordinate(0.0, 1.0), 
				new Koordinate(1.0, 3.0), 
				new Koordinate(2.0, 5.0),
				new Koordinate(3.0, 7.0),
				new Koordinate(4.0, 9.0),
				new Koordinate(5.0, 11.0)
				};
		
		//Act
		Koordinate[] returnedValues = testFunction.calculate(start, end, step);		
		
		//Assert
		for(Integer i=0; i<returnedValues.length; i++)
		{
			Assert.assertEquals("x-Value, position "+ i.toString(), expectedValues[i].getX(), returnedValues[i].getX(), 0.0);
			Assert.assertEquals("y-Value, position"+ i.toString(), expectedValues[i].getX(), returnedValues[i].getX(), 0.0);
		}
	}
	
	@Test
	public void calculatesCorrectValues_SquareFunction()
	{
		//Arrange
		UniversalFunction testFunction = new UniversalFunction("2x^3+1");
		
		double start = 0.0;
		double end = 5.0;
		double step = 1.0;
		
<<<<<<< HEAD
//		double[] expectedValues = new double[] {1.0, 3.0, 5.0, 7.0, 9.0, 11.0};
//		double[] returnedValues = testFunction.calculate(start, end, step);
//		
//		Assert.assertArrayEquals(expectedValues, returnedValues, 0);
=======
		Koordinate[] expectedValues = new Koordinate[] { 
				new Koordinate(0.0, 1.0), 
				new Koordinate(1.0, 3.0), 
				new Koordinate(2.0, 17.0),
				new Koordinate(3.0, 55.0),
				new Koordinate(4.0, 129.0),
				new Koordinate(5.0, 251.0)
				};
		
		//Act
		Koordinate[] returnedValues = testFunction.calculate(start, end, step);
		
		//Assert
		for(Integer i=0; i<returnedValues.length; i++)
		{
			Assert.assertEquals("x-Value, position "+ i.toString(), expectedValues[i].getX(), returnedValues[i].getX(), 0.0);
			Assert.assertEquals("y-Value, position"+ i.toString(), expectedValues[i].getX(), returnedValues[i].getX(), 0.0);
		}
>>>>>>> feature/devLE
	}
	
	@Test
	public void calculate_returnNullInsteadOfException()
	{
		UniversalFunction testFunction = new UniversalFunction("yxcvöasdf");
		Object result = testFunction.calculate(0.0, 10.0, 0.1);
		
		Assert.assertEquals(null, result);
	}
	
	//TODO verhalten wenn 1/0 geteilt wird
}
