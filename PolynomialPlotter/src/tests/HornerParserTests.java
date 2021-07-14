package tests;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import logic.FunctionParsingException;
import logic.HornerParser;

public class HornerParserTests {
	
	@Test
	public void calculatesCorrectValues_LinearFunction()
	{
		HornerParser hP = new HornerParser();
		
		String[] functions = {
				"f(x) = 5x^2+(2x)(2*4x+3x)+x^(2+3)",
				"g(x) = 2x^3*3(3)+(0x+12x^0)-(2*4x+3)*(-x^1)",
				"h(x) = (5x^4*(3x^2+x-5) + x - 1)*(12x^2-2x+3)",
				"i(x) = (5.36x^4*(3x^2+x-5) + x - 1.6)*(-(12.4x^2-2.2x+3))",
				"j(x) = (19 + x^2 - 19x^5)(x - 1)",
				"k(x) = (4x^2+4x+1)/(2x+1)",
				"x^2-6x-16"
				};
		double[][] erwarteteFaktoren = {
		// Grad  0  1  2 ...
				{0, 0, 27, 0, 0, 1},
				{12, 3, 8, 18},
				{-3, 5, -14, 12, -75, 65, -265, 30, 180},
				{4.8, -6.52, 22.04, -12.4, 80.4, -75.04, 295.872, -31.088, -199.392},
				{-19, 19, -1, 1, 0, 19, -19},
				{1, 2},
				{-16, -6, 1}
		};
		
		for(int i = 0; i < functions.length; i++) {
			
			String erwartetString = Arrays.toString(erwarteteFaktoren[i]);
			try {
				String functionString = hP.parse(functions[i]).toString();
				
				// gibt Werte aus
				System.out.println("In: " + functions[i]);
				System.out.println("Out: " + functionString);
				
				// Vergleicht Werte für die function
				Assert.assertEquals(erwartetString, functionString);
			} 
			catch (FunctionParsingException e) {
				System.out.println("Es ist ein unerwarteter Fehler aufgetreten: \n");
				e.printStackTrace();
			}
			catch (AssertionError e) {
				System.out.println("FEHLER!! Erwartet: " + erwartetString);
				throw e;
			}
			System.out.println("");
		}
	}
}
