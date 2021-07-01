package logic;

import java.util.Arrays;

import model.HornerFunction;
import model.IFunction;

public class HornerParser implements IParser {
		
	/*
	 * Enum der Operatoren. 
	 * Reihenfolge muss der umgekehrten Priorisierung entsprechen
	 */
	private enum operator {
		addition,
		subtraktion,
		multiplikation,
		division,
		potenz
	}
	
	@Override
	public IFunction parse(String function) throws Exception {
	
		try {
			return new HornerFunction(this.parseToArray(function));
		}
		catch(Exception e) {
			throw new Exception("'" + function + "' konnte nicht geparsed werden");
		}
	}
	
	/**
	 * Dient den UnitTests bis das endgültige AusgabeObjekt existiert
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public double[] parseToArray(String function) throws Exception {
		String fnct = function.replaceAll("\\s", "");
		
		// Entfernt ggf. den FunctionName
		String[] functionTempArray = fnct.split("\\=");
		if(functionTempArray.length == 2){
			fnct = functionTempArray[1];
		}

		// Prüft, ob Funktion korrekt beginnt
		if(!Character.isDigit(fnct.charAt(0))
				&& fnct.charAt(0) != 'x'
				&& fnct.charAt(0) != '+'
				&& fnct.charAt(0) != '-'
				&& fnct.charAt(0) != '(') {
			throw new Exception("Startwert '" + fnct.charAt(0) + "' ungültig");
		}
		
		return innerParse(fnct.toCharArray(), 0);
	}

	/**
	 * Berechnet Rekursiv das parsedHornerArray
	 * @param function
	 * @param rekCounter
	 * @return
	 * @throws Exception
	 */
	private double[] innerParse(char[] function, int rekCounter) throws Exception {
		
		// Notfallabbruch
		if(rekCounter > 500) {
			throw new Exception("Es wurden zu viele Rekursionsstufen benötigt. Rest-String: '" + new String(function) + "'");
		}
		rekCounter++;
		
		// Prüft, ob function bereits nur Zahlen oder x, versucht ggf. direkt zu parsen
		boolean isDigitsOnly = true;
		for(char character:
			function) {
			if(!Character.isDigit(character)) {
				isDigitsOnly = false;
				break;
			}
		}
		if(function.length == 0
			|| (function.length == 1 && function[0] == 'x')
			|| isDigitsOnly) {
			return getPHAFromFunctionArray(function);
		}
		
		// Rekursives zerteilen der function in atomare Teile entsprechend des Operators
		for(operator op:
				operator.values()) {
			char operatorChar;
			switch (op) {
				case addition:
					operatorChar = '+';
					break;
				case subtraktion:
					operatorChar = '-';
					break;
				case multiplikation:
					operatorChar = '*';
					break;
				case division:
					operatorChar = '/';
					break;
				default:
					operatorChar = '^';
					break;
			}
			
			// gibt an, wie tief der aktuelle Punkt in Klammern ist
			int klammerZahl = 0;
			// gibt die Anzahl der Character an, welche nicht von Klammern eingeschlossen sind
			int unumklammertCount = 0;
			
			for(int i = 0; i < function.length; i++) {
				
				// reduziert ggf. klammerEbene
				if(function[i] == ')') {
					klammerZahl--;
				}
				
				// Funktion darf nur außerhalb einer Klammer getrennt werden
				if(klammerZahl == 0) {

					// prüft, ob der aktuell zu bearbeitende Operator der aktuelle char ist
					if(function[i] == operatorChar) {
						// parsed rekursiv alles links und rechts vom Operator
						
						// linkes Objekt kann mit nächstem Operator bearbeitet werden, da der aktuelle Operator nicht mehr existiert
						double[] pHALeft = innerParse(Arrays.copyOfRange(function, 0, i), rekCounter);
						
						// rechtes Objekt wird weiter auf aktuellen Operator geprüft
						double[] pHARight = innerParse(Arrays.copyOfRange(function, i + 1, function.length), rekCounter);
						
						return combineRechenobjekte(pHALeft, pHARight, op);
					}
					// Prüft ggf., ob eine ungeschriebene Multiplikation vorliegt 
					else if(op == operator.multiplikation
							&& i > 0
							&& (function[i-1] == 'x' && (Character.isDigit(function[i]) || function[i] == '(' || function[i] == 'x')
								|| Character.isDigit(function[i-1]) && (function[i] == 'x' || function[i] == '(')
								|| function[i-1] == ')' && (Character.isDigit(function[i]) || function[i] == 'x' || function[i] == '('))){

						// parsed rekursiv alles links und rechts vom ungeschriebenen Operator
						
						// linkes Objekt kann mit nächstem Operator bearbeitet werden, da der aktuelle Operator nicht mehr existiert
						double[] pHALeft = innerParse(Arrays.copyOfRange(function, 0, i), rekCounter);
						
						// rechtes Objekt wird weiter auf aktuellen Operator geprüft
						double[] pHARight = innerParse(Arrays.copyOfRange(function, i, function.length), rekCounter);
						
						return combineRechenobjekte(pHALeft, pHARight, op);
					}
					
					// erhöht die Anzahl der sich nicht innerhalb Klammern befindlichen Character
					unumklammertCount++;
				}

				// erhöht ggf. klammerEbene
				if(function[i] == '(') {
					klammerZahl++;
				}
			}
			
			// Wenn komplette function in Klammern können diese Klammern aufgelöst werden
			if(unumklammertCount == 2
					&& function[0] == '('
					&& function[function.length - 1] == ')') {
				return innerParse(Arrays.copyOfRange(function, 1, function.length - 1), rekCounter);
			}			
		}
		
		// Wenn alle Operatoren erfolglos durchgearbeitet wurden kann versucht werden das Objekt zu parsen
		return getPHAFromFunctionArray(function);
	}
	
	/**
	 * Parsed das CharArray konkret zu einem parsedHornerArray (nur wenn diese x, bzw double ist)
	 * @param function
	 * @return
	 * @throws Exception 
	 */
	private double[] getPHAFromFunctionArray (char[] function) throws Exception {
		if(function.length == 0) {
			// gibt Array für 0 zurück
			return new double[]{0};
		}
		if((function.length == 1
			&& function[0] == 'x')) {
			// gibt Array für "1*x^1" zurück
			return new double[]{0, 1};
		}
		
		try {
			// gibt Array für "faktor" zurück
			return new double[]{Double.parseDouble(new String(function))};
		}
		catch(Exception e) {
			throw new Exception("String konnte nicht geparsed werden: '" + new String(function) + "'");
		}
	}
	
	/**
	 * Kombiniert die Objekte entsprechend des Operators
	 * @param polyRoLeft
	 * @param polyRoRight
	 * @param op
	 * @return
	 * @throws Exception 
	 */
	private static double[] combineRechenobjekte(double[] pHALeft, double[] pHARight, operator op) throws Exception {

		switch (op) {
			case addition:
				return addition(pHALeft, pHARight);
			case subtraktion:
				return subtraktion(pHALeft, pHARight);
			case multiplikation:
				return multiplikation(pHALeft, pHARight);
			case division:
				return division(pHALeft, pHARight);
			case potenz:
				return potenzierung(pHALeft, pHARight);
		}
		
		throw new Exception("Operator '" + op + "' not implemented.");
	}
	
	private static double[] addition(double[] pHALeft, double[] pHARight) {
		int maxLength = Math.max(pHALeft.length, pHARight.length);
		int minLength = Math.min(pHALeft.length, pHARight.length);
		double[] newPHA = new double[maxLength];
		
		// addiert die Werte, welche in beiden Arrays existieren
		for(int i = 0; i < minLength; i++) {
			newPHA[i] = pHALeft[i] + pHARight[i];
		}
		
		// fügt die restlichen werte des längeren Arrays an
		if(pHALeft.length == maxLength) {
			for(int i = minLength; i < maxLength; i++) {
				newPHA[i] = pHALeft[i];
			}
		}
		else {
			for(int i = minLength; i < maxLength; i++) {
				newPHA[i] = pHARight[i];
			}
		}
		
		return cleanPHA(newPHA);
	}
	
	private static double[] subtraktion(double[] pHALeft, double[] pHARight) {
		double[] newPHA = new double[pHARight.length];
		
		// multipliziert alle Werte in rechten Array *(-1)
		for(int i = 0; i < pHARight.length; i++) {
			newPHA[i] = pHARight[i] * (-1);
		}
		
		return addition(pHALeft, newPHA);
	}
	
	private static double[] multiplikation(double[] pHALeft, double[] pHARight) {
		int newLength = pHALeft.length + pHARight.length - 1;
		double[] newPHA = new double[newLength];
		
		// initialisiert das neue Array
		for(int i = 0; i < newLength; i++) {
			newPHA[i] = 0;
		}
		
		// multipliziert alle Werte
		for(int leftIndex = 0; leftIndex < pHALeft.length; leftIndex++) {
			if(pHALeft[leftIndex] == 0) {
				continue;
			}
			for(int rightIndex = 0; rightIndex < pHARight.length; rightIndex++) {
				// neue Position ist linker+rechterIndex, da dies der multiplikation von x^n entspricht
				newPHA[leftIndex + rightIndex] += pHALeft[leftIndex] * pHARight[rightIndex];
			}
		}
		
		return cleanPHA(newPHA);
	}
	
	private static double[] division(double[] pHALeft, double[] pHARight) throws Exception {
		// TODO TV implementieren
		throw new Exception("Division not Implemented");
	}
	
	private static double[] potenzierung(double[] pHALeft, double[] pHARight) throws Exception {
		if(pHARight.length != 1) {
			throw new Exception("Potenzierung mit x ist nicht erlaubt.");
		}
		
		// TODO TV checken, ob Grad wirklich int
		int grad = (int) Math.round(pHARight[0]);
		double[] newPHA = new double[] {1};
		for(int i = 0; i < grad; i++) {
			newPHA = multiplikation(newPHA, pHALeft);
		}
		
		return cleanPHA(newPHA);
	}
	
	private static double[] cleanPHA(double[] pHA) {
		int i = pHA.length - 1;
		while(i >= 0 
				&& Math.round(pHA[i]) == 0) {
			i--;
		}
		return Arrays.copyOfRange(pHA, 0, i + 1);
	}
}
