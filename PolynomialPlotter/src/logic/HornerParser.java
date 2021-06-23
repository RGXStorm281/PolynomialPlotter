package logic;

import java.util.ArrayList;
import java.util.Arrays;

import model.IFunction;

public class HornerParser implements IParser {
		
	private enum operator {
		DEFAULT,
		addition,
		subtraktion,
		multiplikation,
		division,
		potenz
	}
	
	@Override
	public boolean canParse(String function) {
		// TODO TV testen
		// Überprüft, ob die function dem Format des HornerSchemas entspricht
		function.replaceAll("\\s", "");
		if(function.split("\\(").length == function.split("\\)").length // vergleicht Anzahl der "(" und ")" in dem String
				&& (function.matches("[a-z]*\\(x\\)=\\(*[0-9]*\\*{0,1}(x(\\+[0-9]{1,}){0,1}\\))*x(\\+[0-9]{1,}){0,1}")
				|| function.matches("[a-z]*\\(x\\)=[0-9]*"))) {
			return true;
		}
		return false;
	}
	
	@Override
	public IFunction parse(String function) {
		function.replaceAll("\\s", "");
		
		// Entfernt den FunctionName
		String[] functionTempArray = function.split("\\=");
		if(functionTempArray.length != 2){
			return null;
		}
		
		// Prüft, ob Funktion korrekt beginnt
		if(!Character.isDigit(function.charAt(0))
				&& function.charAt(0) != 'x'
				&& function.charAt(0) != '+'
				&& function.charAt(0) != '-') {
			return null;
		}
		
		try {
			var a = innerParse(function.toCharArray(), operator.addition, 0);
		}
		catch(Exception e) {
			// TODO TV Exceptionhandlling
		}
		
		// TODO TV korrektes Objekt zurückgeben
		return null;
	}
	
	private PolyRechenobjekt innerParse(char[] function, operator op, int rekCounter) throws Exception {
		
		// Wenn alle Operatoren durchgearbeitet wurden kann versucht werden das Objekt zu parsen
		if(op == operator.DEFAULT) {
			return getRechenobjFromArray(function);
		}

		// Notfallabbruch
		if(rekCounter > 500) {
			throw new Exception("Es wurden zu viele Rekursionsstufen benötigt. Rest-String: '" + new String(function) + "'");
		}
		rekCounter++;
		
		// Rekursives zwerteilen der function in atomare Teile
		
		// gibt an, wie tief der aktuelle Punkt in Klammern ist
		int klammerZahl = 0;
		// gibt die Anzahl der Character an, für die KlammerZahl != null gilt um zu ermitteln, wann Klammer aufgelöst werden kann
		int charsInKlammern = 0;
		
		for(int i = 0; i < function.length; i++) {
			
			if(function[i] == '(') {
				klammerZahl++;
			}
			else if(function[i] == ')') {
				klammerZahl--;
			}

			// Funktion darf nur außerhalb einer Klammer getrennt werden
			if(klammerZahl == 0) {
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

				// prüft, ob der aktuell zu bearbeitende Operator der aktuelle char ist
				if(function[i] == operatorChar) {
					// parsed rekursiv alles links und rechts vom Operator
					
					// linkes Objekt kann mit nächstem Operator bearbeitet werden, da der aktuelle Operator nicht mehr existiert
					PolyRechenobjekt polyRoLeft = innerParse(Arrays.copyOfRange(function, 0, i - 1), getNextOperator(op), rekCounter);
					
					// rechtes Objekt wird weiter auf aktuellen Operator geprüft
					PolyRechenobjekt polyRoRight = innerParse(Arrays.copyOfRange(function, i + 1, function.length - 1), op, rekCounter);
					
					return combineRechenobjekte(polyRoLeft, polyRoRight, op);
				}
				// Prüft ggf., ob eine ungeschriebene Multiplikation vorliegt 
				else if(op == operator.multiplikation
						&& i > 0
						&& (function[i-1] == 'x' && (Character.isDigit(function[i]) || function[i] == '(' || function[i] == 'x')
							|| Character.isDigit(function[i-1]) && (function[i] == 'x' || function[i] == '(')
							|| function[i-1] == ')' && (Character.isDigit(function[i]) || function[i] == 'x' || function[i] == '('))){

					// parsed rekursiv alles links und rechts vom ungeschriebenen Operator
					
					// linkes Objekt kann mit nächstem Operator bearbeitet werden, da der aktuelle Operator nicht mehr existiert
					PolyRechenobjekt polyRoLeft = innerParse(Arrays.copyOfRange(function, 0, i - 1), getNextOperator(op), rekCounter);
					
					// rechtes Objekt wird weiter auf aktuellen Operator geprüft
					PolyRechenobjekt polyRoRight = innerParse(Arrays.copyOfRange(function, i, function.length - 1), op, rekCounter);
					
					return combineRechenobjekte(polyRoLeft, polyRoRight, op);
				}
			}
			else {
				// erhöht die Anzahl der in der Klammer befindlichen Character
				charsInKlammern++;
			}
		}
		
		// Wenn komplette function in Klammern kann diese aufgelöst werden
		if(charsInKlammern == function.length - 1
				&& function[0] == '('
				&& function[function.length - 1] == ')') {
			return innerParse(Arrays.copyOfRange(function, 1, function.length - 2), operator.addition, rekCounter);
		}
		
		// Wenn komplette function durchgegangen ohne zu trennen wird ggf. mit nächstem Operator versucht
		return innerParse(function, getNextOperator(op), rekCounter);
	}
	
	/**
	 * gibt den folgenden Operator zurück
	 * @param op
	 * @return
	 */
	private operator getNextOperator(operator op) {
		switch (op) {
			case addition:
				return operator.subtraktion;
			case subtraktion:
				return operator.multiplikation;
			case multiplikation:
				return operator.division;
			case division:
				return operator.potenz;
			default:
				return operator.DEFAULT;
		}
	}
	
	/**
	 * Kombiniert die Objekte entsprechend des Operators
	 * @param polyRoLeft
	 * @param polyRoRight
	 * @param op
	 * @return
	 */
	private PolyRechenobjekt combineRechenobjekte(PolyRechenobjekt polyRoLeft, PolyRechenobjekt polyRoRight, operator op) {
		switch (op) {
			case addition:
				polyRoLeft.add(polyRoRight);
				break;
			case subtraktion:
				polyRoLeft.sub(polyRoRight);
				break;
			case multiplikation:
				polyRoLeft.mult(polyRoRight);
				break;
			case division:
				polyRoLeft.div(polyRoRight);
				break;
			case potenz:
				polyRoLeft.pot(polyRoRight);
				break;
		}
		
		return polyRoLeft;
	}
	
	/**
	 * Parsed das CharArray zu einem PolyRechenobjekt;
	 * @param function
	 * @return
	 * @throws Exception 
	 */
	private PolyRechenobjekt getRechenobjFromArray (char[] function) throws Exception {
		if(function.length == 0) {
			// gibt leeres objekt zurück
			return new PolyRechenobjekt(0, 0);
		}
		if((function.length == 1
			&& function[0] == 'x')) {
			// gibt Rechenobjekt "1*x^1" zurück
			return new PolyRechenobjekt(1, 1);
		}
		
		// Prüft, ob function nur aus Zahlen besteht
		for(char character:
			function) {
			if(!Character.isDigit(character)) {
				throw new Exception("String konnte nicht geparsed werden: '" + new String(function) + "'");
			}
		}
		
		// gibt Rechenobjekt "faktor*x^0" zurück
		return new PolyRechenobjekt(Double.parseDouble(new String(function)), 0);
	}
	
	/**
	 * Aufbau: faktor*x^potenz + PolynomRechenobjekt
	 * Die Objekte sind nach Potenz absteigend sortiert => a*x^n + b*x^(n-m) + ...; n>m; n,m € N
	 * @author timo.vollert
	 *
	 */
	private class PolyRechenobjekt{
		private double faktorValue;
		private int potenzValue;
		private PolyRechenobjekt addedObjekt;
		
		public PolyRechenobjekt(double faktor, int potenz, PolyRechenobjekt addedPolyRechenobjekt) {
			this.faktorValue = faktor;
			this.potenzValue = potenz;
			this.addedObjekt = addedPolyRechenobjekt;
		}

		public PolyRechenobjekt(double faktor, int potenz) {
			this.faktorValue = faktor;
			this.potenzValue = potenz;
			this.addedObjekt = null;
		}
		
		public PolyRechenobjekt(PolyRechenobjekt polyRechenobjekt) {
			this.faktorValue = polyRechenobjekt.getFaktor();
			this.potenzValue = polyRechenobjekt.getPotenz();
			this.addedObjekt = null;
		}
		
		public double getFaktor() {
			return this.faktorValue;
		}
		
		public int getPotenz() {
			return this.potenzValue;
		}
		
		public PolyRechenobjekt getAddedObjekt() {
			return this.addedObjekt;
		}
		
		/**
		 * Addiert das polyRechenobjekt zu dem aktuellen Polyrechenobjekt (zerstört das mitgegebene Objekt)
		 */
		public void add(PolyRechenobjekt polyRechenobjekt) {
			
			while (polyRechenobjekt != null) {
				
				if(polyRechenobjekt.getPotenz() > this.potenzValue) {
					// das neue Objekt hat eine höhere Potenz, wird deshalb neuer HEAD des Objekts
					this.addedObjekt =  new PolyRechenobjekt(this.faktorValue, this.potenzValue, this.addedObjekt);
					this.faktorValue = polyRechenobjekt.getFaktor();
					this.potenzValue = polyRechenobjekt.getPotenz();
				}
				else if(this.potenzValue == polyRechenobjekt.getPotenz()) {
					// Wenn zu addierendes Objekt gleichePotenz können einfach die Faktoren der objekte addiert werden
					this.faktorValue += polyRechenobjekt.getFaktor();
				}
				else if(this.addedObjekt != null) {
					// Wenn noch weitere addierte objekte existieren wird versucht mit ihnen zu addieren
					this.addedObjekt.add(polyRechenobjekt);
				}
				else {
					// Wenn kein weiteres addiertes Objekt existiert wird das Objekt als neuer TAIL angefügt
					this.addedObjekt = polyRechenobjekt;
				}
				
				// Wiederhole Vorgang rekursiv (durch vorsortierung kann dies in der Rekursion geschehen)
				polyRechenobjekt = polyRechenobjekt.getAddedObjekt();
			}
		}

		/**
		 * Subtrahiert das polyRechenobjekt von dem aktuellen Polyrechenobjekt (zerstört das mitgegebene Objekt)
		 */
		public void sub(PolyRechenobjekt polyRechenobjekt) {
			
			// Dreht Vorzeichen aller Faktoren um bevor diese addiert werden
			while (polyRechenobjekt != null) {
				this.add(new PolyRechenobjekt(polyRechenobjekt.getFaktor() * (-1), polyRechenobjekt.getPotenz()));
				polyRechenobjekt = polyRechenobjekt.getAddedObjekt();
			}
		}
		
		/**
		 * Multipliziert das polyRechenobjekt mit dem aktuelle Polyrechenobjekt (zerstört das mitgegebene Objekt)
		 */
		public void mult(PolyRechenobjekt polyRechenobjekt) {
			
			this.innerMult(polyRechenobjekt);
			
			// Sortieren und Zusammenfassen
			this.bucketSort();
		}
		
		/**
		 * Dividiert das polyRechenobjekt mit dem aktuelle Polyrechenobjekt (zerstört das mitgegebene Objekt)
		 */
		public void div(PolyRechenobjekt polyRechenobjekt) {
			
			// TODO TV implementieren
		}
		
		/**
		 * Sortiert die Rechenobjekte und fügt Rechenobjekte mit der gleichen Potenz zusammen
		 */
		private void bucketSort() {
			
			// Buckets initialisieren
			ArrayList<PolyRechenobjekt>[] buckets = new ArrayList[this.potenzValue + 1];
			for(ArrayList aL:
				buckets) {
				aL = new ArrayList();
			}
			
			// Streuen
			PolyRechenobjekt tempPolyRo = this;
			while(tempPolyRo != null) {
				buckets[tempPolyRo.getPotenz()].add(new PolyRechenobjekt(tempPolyRo));
				tempPolyRo = tempPolyRo.getAddedObjekt();
			}
			
			// Sammeln (von niedrig nach hoch um Vergleiche zu minimieren)
			tempPolyRo = new PolyRechenobjekt(0, 0);
			for(ArrayList<PolyRechenobjekt> bucket:
				buckets) {
				for(PolyRechenobjekt polyRo:
					bucket) {
					tempPolyRo.add(polyRo);
				}
			}
			
			// Speichern
			this.faktorValue = tempPolyRo.getFaktor();
			this.potenzValue = tempPolyRo.getPotenz();
			this.addedObjekt = tempPolyRo.getAddedObjekt();
		}
		
		private void innerMult(PolyRechenobjekt polyRechenobjekt) {
			
			// Multipliziert jedes Rechenobjekt des ersten Objekts mit jedem Rechenobjekt des zweiten Objekts
			while (polyRechenobjekt != null) {
				if (this.addedObjekt != null) {
					this.addedObjekt.innerMult(polyRechenobjekt);
				}
				
				this.faktorValue = this.faktorValue * polyRechenobjekt.getFaktor();
				this.potenzValue = this.potenzValue + polyRechenobjekt.getPotenz();
				
				polyRechenobjekt = polyRechenobjekt.getAddedObjekt();
			}
			
		}

		/**
		 * Potenziert das polyRechenobjekt mit dem aktuelle Polyrechenobjekt (zerstört das mitgegebene Ojekt)
		 */
		public void pot(PolyRechenobjekt polyRechenobjekt) {
			// TODO TV abfangen, wenn x hoch != 0 oder !€ N
			
			PolyRechenobjekt clonedPolyRo = this.clone();
			int potenz = (int)polyRechenobjekt.getFaktor();
			for(int i = 0; i < potenz; i++) {
				this.innerMult(clonedPolyRo);
			}
			
			// Sortieren und Zusammenfassen
			this.bucketSort();
		}
		
		/**
		 * Erstellt ein neues PolyRechenobjekt mit den gleichen Werten
		 */
		public PolyRechenobjekt clone() {
			if(this.addedObjekt == null) {
				return new PolyRechenobjekt(this);
			}
			else {
				return new PolyRechenobjekt(this.faktorValue, this.potenzValue, this.addedObjekt.clone());
			}
		}
	}
}
