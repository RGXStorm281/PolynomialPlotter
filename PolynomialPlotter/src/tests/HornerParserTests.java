package tests;

import org.junit.Assert;
import org.junit.Test;

import logic.HornerParser;
import logic.HornerParser.PolyRechenobjekt;

public class HornerParserTests {
	
	@Test
	public void calculatesCorrectValues_LinearFunction()
	{
		HornerParser hP = new HornerParser();
		PolyRechenobjekt polyRo = null;
		
		String function = "f(x) = 5x^2+(2x)(2*4x+3x)+x^(2+3)";
		function = function.replaceAll("\\s", "");
		
		// Entfernt den FunctionName
		String[] functionTempArray = function.split("\\=");
		if(functionTempArray.length == 2){
			function = functionTempArray[1];
			
			// Prüft, ob Funktion korrekt beginnt
			if(Character.isDigit(function.charAt(0))
					|| function.charAt(0) == 'x'
					|| function.charAt(0) == '+'
					|| function.charAt(0) == '-') {
				
				try {
					polyRo = hP.innerParse(function.toCharArray(), 0);
				}
				catch(Exception e) {
					// TODO TV Exceptionhandlling
					System.out.println(e);
				}
				
				double[] erwarteteFaktoren = {
						1,
						27
				};
				int[] erwartetePotenzen = {
						5,
						2
				};
				int rekursionstiefe = 0;
				try{
					Assert.assertEquals(true, polyRo != null);
					while (polyRo != null) {
						Assert.assertEquals(erwarteteFaktoren[rekursionstiefe], polyRo.getFaktor(), 0.01);
						Assert.assertEquals(erwartetePotenzen[rekursionstiefe], polyRo.getPotenz());
					}
				}
				catch (Exception e) {
					Assert.assertEquals(e, null);
				}
			}
		}
	}
}
