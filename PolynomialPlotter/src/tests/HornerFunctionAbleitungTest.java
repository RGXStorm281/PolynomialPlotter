package tests;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import model.HornerFunction;
import model.Koordinate;


public class HornerFunctionAbleitungTest {
	
	@Test
	public void testAbleitung_HornerFunction(){
		//Arrange
		double[][] testElements = {
		// Grad  0  1  2 ...
				{0, 0, 27, 0, 0, 1},
				{12, 3, 8, 18},
				{-2, -3, -4, -5}
		};	
		
		double[][] erwarteteFaktoren = {
		// Grad  0  1  2 ...
				{0, 54, 0, 0, 5},
				{3, 16, 54},
				{-3, -8, -15}
		};
		
		for(int i = 0; i < testElements.length; i++){
			HornerFunction testFunction = new HornerFunction("f", "no display", testElements[i]);
			var a = testFunction.getAbleitung().toString();
			var b = Arrays.toString(erwarteteFaktoren[i]);

			Assert.assertEquals(b.toString(), a);
		}
	}

}
