package logic;

import java.util.Arrays;
import java.util.logging.Logger;

import model.HornerFunction;

public class HornerParser implements IParser {
		
	/*
	 * Enum der Operatoren. 
	 * Reihenfolge muss der umgekehrten Priorisierung entsprechen
	 */
	private static char[] operators = {'+', '-', '*', '/', '^'};
	
	@Override
	public HornerFunction parse(String functionName, String function) throws FunctionParsingException {
	
		try {
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
				throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "Startwert '" + fnct.charAt(0) + "' ungültig");
			}
			
			double[] hornerElementArray = innerParse(fnct.toCharArray(), 0, fnct.length(), 0, 0);
			
			return new HornerFunction(functionName, function, hornerElementArray);
		}
		catch(Exception e) {
			Logger.getGlobal().severe(e.getMessage());
			throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "'" + function + "' konnte nicht geparsed werden");
		}
	}

	/**
	 * Berechnet rekursiv das parsedHornerArray
	 * @param function
	 * @param rekCounter
	 * @return
	 * @throws Exception
	 */
	private double[] innerParse(char[] function, int startIndex, int length, int operator, int rekCounter) throws Exception {		
		// TODO TV Addition-Subtraktion und Multiplikation-Division zusammenführen
		// TODO TV ggf. Operator-Positionen zu Beginn bestimmen um das nicht immer wieder aufs neue ermitteln zu müssen
		
		// Notfallabbruch
		if(rekCounter > 500) {
			throw new Exception("Es wurden zu viele Rekursionsstufen benötigt. Rest-String: '" + new String(Arrays.copyOfRange(function, startIndex, startIndex + length)) + "'");
		}
		rekCounter++;
		
		int endIndex = startIndex + length - 1;
		
		// Prüft, ob function bereits nur Zahlen oder x, versucht ggf. direkt zu parsen
		boolean isDigitsOnly = true;
		for(int i = startIndex; i <= endIndex; i++) {
			if(!Character.isDigit(function[i])
				&& function[i] != '.') {
				isDigitsOnly = false;
				break;
			}
			
		}
		if(isDigitsOnly
			|| length == 0
			|| (length == 1 && function[startIndex] == 'x')) {
			return getPHAFromFunctionArray(function, startIndex, length);
		}
		
		if(this.isFunctionUmklammert(function, startIndex, endIndex)) {
			// Wenn gesamter functionteil in Klammern ist, dann wird der Teil darin ohne diese äußeren Klammern geparsed
			return innerParse(function, startIndex + 1, length - 2, 0, rekCounter);
		}
		
		// Rekursives zerteilen der function in atomare Teile entsprechend des Operators
		for(int operatorIndex = operator; operatorIndex < operators.length; operatorIndex++) {
			
			// gibt an, wie tief der aktuelle Punkt in Klammern ist
			int klammerZahl = 0;
			
			for(int i = endIndex; i >= startIndex; i--) {
				
				// reduziert ggf. klammerEbene
				if(function[i] == '(') {
					klammerZahl--;
				}
				
				// Funktion darf nur außerhalb einer Klammer getrennt werden
				if(klammerZahl == 0) {

					// prüft, ob der aktuell zu bearbeitende Operator der aktuelle char ist
					if(function[i] == operators[operatorIndex]) {
						
						int leftLength = i - startIndex;
						
						// parsed rekursiv alles links und rechts vom Operator 
						// rechte rekursion kann mit nächstem Operator vorgesetzt werden
						double[] pHALeft = innerParse(function, startIndex, leftLength, 0, rekCounter);
						double[] pHARight = innerParse(function, i + 1, length - leftLength - 1, operatorIndex + 1, rekCounter);
						
						return combineRechenobjekte(pHALeft, pHARight, operatorIndex);
					}
					// Prüft ggf., ob eine ungeschriebene Multiplikation vorliegt 
					else if(operators[operatorIndex] == '*'
							&& i > startIndex
							&& (function[i-1] == 'x' && (Character.isDigit(function[i]) || function[i] == 'x' || function[i] == '(')
								|| Character.isDigit(function[i-1]) && (function[i] == 'x' || function[i] == '(')
								|| function[i-1] == ')' && (Character.isDigit(function[i]) || function[i] == 'x' || function[i] == '('))){

						int leftLength = i - startIndex;
						
						// parsed rekursiv alles links und rechts vom ungeschriebenen Operator
						double[] pHALeft = innerParse(function, startIndex, leftLength, 0, rekCounter);
						double[] pHARight = innerParse(function, i, length - leftLength, operatorIndex + 1, rekCounter);
						
						return combineRechenobjekte(pHALeft, pHARight, operatorIndex);
					}
				}

				// erhöht ggf. klammerEbene
				if(function[i] == ')') {
					klammerZahl++;
				}
			}		
		}

		// Wenn alle Operatoren erfolglos durchgearbeitet wurden ist das parsen gescheitert
		throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "String konnte nicht geparsed werden: '" + new String(Arrays.copyOfRange(function, startIndex, startIndex + length)) + "'");
	}

	/**
	 * Ermittelt, ob der aktuelle Functionsteil vollständig umklammert ist.
	 * @param function
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private boolean isFunctionUmklammert(char[] function, int startIndex, int endIndex) {
		
		// gibt an, wie tief der aktuelle Punkt in Klammern ist
		int klammerZahl = 0;
		// gibt die Anzahl der Character an, welche nicht von Klammern eingeschlossen sind
		int unumklammertCount = 0;
		
		for(int i = endIndex; i >= startIndex; i--) {
			
			// reduziert ggf. klammerEbene
			if(function[i] == '(') {
				klammerZahl--;
			}
			
			// Funktion darf nur außerhalb einer Klammer getrennt werden
			if(klammerZahl == 0) {
				// erhöht die Anzahl der sich nicht innerhalb Klammern befindlichen Character
				unumklammertCount++;
			}

			// erhöht ggf. klammerEbene
			if(function[i] == ')') {
				klammerZahl++;
			}
		}
		
		// Wenn komplette function in Klammern können diese Klammern aufgelöst werden
		if(unumklammertCount == 2
				&& function[startIndex] == '('
				&& function[endIndex] == ')') {
			return true;
		}
		
		return false;
	}

	/**
	 * Parsed das CharArray konkret zu einem parsedHornerArray (nur wenn diese x, bzw double ist)
	 * @param function
	 * @return
	 * @throws Exception 
	 */
	private double[] getPHAFromFunctionArray (char[] function, int startIndex, int length) throws Exception {
		if(length == 0) {
			// gibt Array für 0 zurück
			return new double[]{0};
		}
		if((length == 1
			&& function[startIndex] == 'x')) {
			// gibt Array für "1*x^1" zurück
			return new double[]{0, 1};
		}
		
		String functionString = new String(Arrays.copyOfRange(function, startIndex, startIndex + length));
		try {
			// gibt Array für "faktor" zurück
			return new double[]{Double.parseDouble(functionString)};
		}
		catch(Exception e) {
			throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "String konnte nicht geparsed werden: '" + functionString + "'");
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
	private static double[] combineRechenobjekte(double[] pHALeft, double[] pHARight, int opIndex) throws Exception {

		switch (operators[opIndex]) {
			case '+':
				return addition(pHALeft, pHARight);
			case '-':
				return subtraktion(pHALeft, pHARight);
			case '*':
				return multiplikation(pHALeft, pHARight);
			case '/':
				return division(pHALeft, pHARight);
			case '^':
				return potenzierung(pHALeft, pHARight);
			default:
				throw new Exception("Operator '" + opIndex + "' not implemented.");
		}
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
		return addition(pHALeft, multiplikation(pHARight, new double[]{-1}));
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
		pHARight = cleanPHA(pHARight);
		int gradRight = pHARight.length - 1;
		if(gradRight < 0
			|| pHARight[0] == 0) {
			throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "Division durch 0 nicht erlaubt.");
		}

		int newLength = pHALeft.length - pHARight.length + 1;
		double[] newPHA = new double[newLength];
		
		// initialisiert das neue Array
		for(int i = 0; i < newLength; i++) {
			newPHA[i] = 0;
		}
		
		double[] pHALeftClone = pHALeft.clone();
		
		// dividieren alle Werte
		for(int leftIndex = pHALeftClone.length - 1; leftIndex >= 0; leftIndex--) {
			if(pHALeftClone[leftIndex] == 0) {
				continue;
			}

			// ermittle wie oft höchster Grad von R in aktuellen Grad von L passt (pHALeft[leftIndex], pHARight[gradRight] != 0)
			double faktor = pHALeftClone[leftIndex] / pHARight[gradRight];
			int gradNew = leftIndex - gradRight;
			if(gradNew < 0) {
				throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "Negative Hochzahlen nicht erlaubt.");
			}

			// hinterlegen des errechneten Wertes
			newPHA[gradNew] = faktor;
			
			// ziehe das faktor-vielfache von R von L ab
			for(int i = 0; i < pHARight.length; i++) {
				
				int gradToEdit = leftIndex - i;
				int rightIndex = gradRight - i;

				pHALeftClone[gradToEdit] = pHALeftClone[gradToEdit] - faktor * pHARight[rightIndex];
			}
		}
		
		return cleanPHA(newPHA);
	}
	
	private static double[] potenzierung(double[] pHALeft, double[] pHARight) throws Exception {
		if(pHARight.length != 1 
				|| pHARight[0] != (int) Math.round(pHARight[0])
				|| pHARight[0] < 0) {
			throw new Exception("Potenzierung ist nur mit natürlichen Zahlen erlaubt.");
		}
		
		int grad = (int) pHARight[0];
		double[] newPHA = new double[] {1};
		for(int i = 0; i < grad; i++) {
			newPHA = multiplikation(newPHA, pHALeft);
		}

		return newPHA;
	}
	
	/**
	 * Reduziert das Array auf den benötigten Grad.
	 * @param pHA
	 * @return
	 */
	private static double[] cleanPHA(double[] pHA) {
		int i = pHA.length - 1;
		while(i >= 0 
				&& Math.round(pHA[i]) == 0) {
			i--;
		}
		return Arrays.copyOfRange(pHA, 0, i + 1);
	}
}
