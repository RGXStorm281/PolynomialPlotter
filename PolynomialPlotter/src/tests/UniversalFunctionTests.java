package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.UniversalFunction;

public class UniversalFunctionTests {

	@Before
	public void setup()
	{
		
	}
	
	@Test
	public void calculatesCorrectValues()
	{
		UniversalFunction testFunction = new UniversalFunction("2x+1");
		
		double start = 0.0;
		double end = 5.0;
		double step = 1.0;
		
		double[] expectedValues = new double[] {1.0, 3.0, 5.0, 7.0, 9.0, 11.0};
		double[] returnedValues = testFunction.calculate(start, end, step);
		
		Assert.assertArrayEquals(expectedValues, returnedValues, 0);
	}
}
