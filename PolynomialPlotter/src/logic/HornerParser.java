package logic;

import java.util.Arrays;
import java.util.List;

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
		function = functionTempArray[1];
		
		// Prüft, ob Funktion korrekt beginnt
		if(!Character.isDigit(function.charAt(0))
				&& function.charAt(0) != 'x'
				&& function.charAt(0) != '+'
				&& function.charAt(0) != '-') {
			return null;
		}
		
		try {
			var parsedObject = innerParse(function.toCharArray(), 0);
		}
		catch(Exception e) {
			// TODO TV Exceptionhandlling
		}
		
		// TODO TV korrektes Objekt zurückgeben
		return null;
	}
	
	/**
	 * Dient den UnitTests bis das endg�ltige AusgabeObjekt existiert
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public PolyRechenobjekt parseTest(String function) throws Exception {
		String fnct = function.replaceAll("\\s", "");
		
		// Entfernt ggf. den FunctionName
		String[] functionTempArray = fnct.split("\\=");
		if(functionTempArray.length == 2){
			fnct = functionTempArray[1];
		}

		// Pr�ft, ob Funktion korrekt beginnt
		if(!Character.isDigit(fnct.charAt(0))
				&& fnct.charAt(0) != 'x'
				&& fnct.charAt(0) != '+'
				&& fnct.charAt(0) != '-'
				&& fnct.charAt(0) != '(') {
			throw new Exception("Startwert '" + fnct.charAt(0) + "' ung�ltig");
		}
		
		PolyRechenobjekt pRo = innerParse(fnct.toCharArray(), 0);
		pRo = this.cleanup(pRo);
		return pRo;
	}
	
	/**
	 * Erstellt aus einer vorsortierten Liste eine Liste mit jedem Grad von 0 bis maxGrad
	 * @param pRo
	 * @return
	 */
	private PolyRechenobjekt cleanup(PolyRechenobjekt pRo) {
		PolyRechenobjekt pRoClone = pRo.clone();
		pRoClone.bucketSort();
		int potenzMax = pRo.getPotenz();
		PolyRechenobjekt pRoNew = new PolyRechenobjekt(pRoClone.getFaktor(), pRoClone.getPotenz());
		pRoClone = pRoClone.getChild();
		for(int currentPotenz = potenzMax - 1; currentPotenz >= 0; currentPotenz--) {
			
			// f�gt f�r jeden Grad < potenzMax einen Eintrag hinzu, ggf. mit Faktor 0
			if(pRoClone == null 
				|| pRoClone.getPotenz() < currentPotenz) {
				// faktor = 0, da f�r diesen Grad kein Eintrag ermittelt wurde
				pRoNew.addTail(new PolyRechenobjekt(0, currentPotenz));
			}
			else {
				pRoNew.addTail(new PolyRechenobjekt(pRoClone));
				pRoClone = pRoClone.getChild();
			}
		}
		
		return pRoNew;
	}

	/**
	 * Berechnet Rekursiv das PolyRechenobjekt
	 * @param function
	 * @param rekCounter
	 * @return
	 * @throws Exception
	 */
	private PolyRechenobjekt innerParse(char[] function, int rekCounter) throws Exception {
		
		// Notfallabbruch
		if(rekCounter > 500) {
			throw new Exception("Es wurden zu viele Rekursionsstufen ben�tigt. Rest-String: '" + new String(function) + "'");
		}
		rekCounter++;
		
		// Pr�ft, ob function bereits nur Zahlen oder x, versucht ggf. direkt zu parsen
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
			return getRechenobjFromArray(function);
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
				
				// Funktion darf nur au�erhalb einer Klammer getrennt werden
				if(klammerZahl == 0) {

					// pr�ft, ob der aktuell zu bearbeitende Operator der aktuelle char ist
					if(function[i] == operatorChar) {
						// parsed rekursiv alles links und rechts vom Operator
						
						// linkes Objekt kann mit n�chstem Operator bearbeitet werden, da der aktuelle Operator nicht mehr existiert
						PolyRechenobjekt polyRoLeft = innerParse(Arrays.copyOfRange(function, 0, i), rekCounter);
						
						// rechtes Objekt wird weiter auf aktuellen Operator gepr�ft
						PolyRechenobjekt polyRoRight = innerParse(Arrays.copyOfRange(function, i + 1, function.length), rekCounter);
						
						return combineRechenobjekte(polyRoLeft, polyRoRight, op);
					}
					// Pr�ft ggf., ob eine ungeschriebene Multiplikation vorliegt 
					else if(op == operator.multiplikation
							&& i > 0
							&& (function[i-1] == 'x' && (Character.isDigit(function[i]) || function[i] == '(' || function[i] == 'x')
								|| Character.isDigit(function[i-1]) && (function[i] == 'x' || function[i] == '(')
								|| function[i-1] == ')' && (Character.isDigit(function[i]) || function[i] == 'x' || function[i] == '('))){

						// parsed rekursiv alles links und rechts vom ungeschriebenen Operator
						
						// linkes Objekt kann mit n�chstem Operator bearbeitet werden, da der aktuelle Operator nicht mehr existiert
						PolyRechenobjekt polyRoLeft = innerParse(Arrays.copyOfRange(function, 0, i), rekCounter);
						
						// rechtes Objekt wird weiter auf aktuellen Operator gepr�ft
						PolyRechenobjekt polyRoRight = innerParse(Arrays.copyOfRange(function, i, function.length), rekCounter);
						
						return combineRechenobjekte(polyRoLeft, polyRoRight, op);
					}
					
					// erh�ht die Anzahl der sich nicht innerhalb Klammern befindlichen Character
					unumklammertCount++;
				}

				// erh�ht ggf. klammerEbene
				if(function[i] == '(') {
					klammerZahl++;
				}
			}
			
			// Wenn komplette function in Klammern k�nnen diese Klammern aufgel�st werden
			if(unumklammertCount == 2
					&& function[0] == '('
					&& function[function.length - 1] == ')') {
				return innerParse(Arrays.copyOfRange(function, 1, function.length - 1), rekCounter);
			}			
		}
		
		// Wenn alle Operatoren erfolglos durchgearbeitet wurden kann versucht werden das Objekt zu parsen
		return getRechenobjFromArray(function);
	}
	
	/**
	 * Kombiniert die Objekte entsprechend des Operators
	 * @param polyRoLeft
	 * @param polyRoRight
	 * @param op
	 * @return
	 */
	private static PolyRechenobjekt combineRechenobjekte(PolyRechenobjekt polyRoLeft, PolyRechenobjekt polyRoRight, operator op) {
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
	 * Parsed das CharArray konkret zu einem PolyRechenobjekt (nur wenn diese x, bzw double ist)
	 * @param function
	 * @return
	 * @throws Exception 
	 */
	private PolyRechenobjekt getRechenobjFromArray (char[] function) throws Exception {
		if(function.length == 0) {
			// gibt leeres objekt zur�ck
			return new PolyRechenobjekt(0, 0);
		}
		if((function.length == 1
			&& function[0] == 'x')) {
			// gibt Rechenobjekt "1*x^1" zur�ck
			return new PolyRechenobjekt(1, 1);
		}
		
		try {
			// gibt Rechenobjekt "faktor*x^0" zur�ck
			return new PolyRechenobjekt(Double.parseDouble(new String(function)), 0);
		}
		catch(Exception e) {
			throw new Exception("String konnte nicht geparsed werden: '" + new String(function) + "'");
		}
	}
	
	/**
	 * Aufbau: faktor*x^potenz + PolynomRechenobjekt
	 * Die Objekte sind nach Potenz absteigend sortiert => a*x^n + b*x^(n-m) + ...; n>m; n,m � N
	 * @author timo.vollert
	 *
	 */
	public class PolyRechenobjekt{
		// TODO private machen
		
		private double faktorValue;
		private int potenzValue;
		private PolyRechenobjekt child; // via Addition verkn�pft
		
		public PolyRechenobjekt(double faktor, int potenz, PolyRechenobjekt addedPolyRechenobjekt) {
			this.faktorValue = faktor;
			this.potenzValue = potenz;
			this.child = addedPolyRechenobjekt;
		}

		public PolyRechenobjekt(double faktor, int potenz) {
			this.faktorValue = faktor;
			this.potenzValue = potenz;
			this.child = null;
		}
		
		public PolyRechenobjekt(PolyRechenobjekt polyRechenobjekt) {
			this.faktorValue = polyRechenobjekt.getFaktor();
			this.potenzValue = polyRechenobjekt.getPotenz();
			this.child = null;
		}
		
		public double getFaktor() {
			return this.faktorValue;
		}
		
		public int getPotenz() {
			return this.potenzValue;
		}
		
		public PolyRechenobjekt getChild() {
			return this.child;
		}
		
		/**
		 * F�gt das objekt als TAIL an die Liste an
		 * @param newChild
		 */
		public void addTail(PolyRechenobjekt newChild) {
			if(this.child == null) {
				this.child = newChild;
			}
			else {
				this.child.addTail(newChild);
			}
		}
		
		/**
		 * Addiert das polyRechenobjekt zu dem aktuellen Polyrechenobjekt (zerst�rt mitgegebenes Objekt)
		 * Optimiert Liste automatisch (addieren zweier vorsortierter Listen resultiert in sortierter Liste)
		 */
		public void add(PolyRechenobjekt polyRechenobjekt) {

			while (polyRechenobjekt != null) {
				
				// addiert nur, wenn neues Objekt Faktor != 0 hat
				if(polyRechenobjekt.getFaktor() != 0) {
					
					// Fallunterscheidung
					if(polyRechenobjekt.getPotenz() > this.potenzValue) {
						// das neue Objekt hat eine h�here Potenz, wird deshalb neuer HEAD des Objekts
						// Wenn der Faktor des aktuellen Objekts 0 ist wird dieses verworfen
						if(this.faktorValue != 0) {
							this.child =  new PolyRechenobjekt(this.faktorValue, this.potenzValue, this.child);
						}
						this.faktorValue = polyRechenobjekt.getFaktor();
						this.potenzValue = polyRechenobjekt.getPotenz();
					}
					else if(this.potenzValue == polyRechenobjekt.getPotenz()) {
						// Wenn zu addierendes Objekt gleichePotenz k�nnen einfach die Faktoren der objekte addiert werden
						this.faktorValue += polyRechenobjekt.getFaktor();
					}
					else if(this.child != null) {
						// Wenn noch weitere addierte objekte existieren wird versucht mit ihnen zu addieren
						// Wenn der Faktor des aktuellen Objekts 0 ist wird dieses verworfen
						if(this.faktorValue == 0) {
							this.faktorValue = this.child.getFaktor();
							this.potenzValue = this.child.getPotenz();
							this.child = this.child.getChild();
						}
						else {
							this.child.add(new PolyRechenobjekt(polyRechenobjekt));
						}
					}
					else {
						// Wenn kein weiteres addiertes Objekt existiert wird das Objekt als neuer TAIL angef�gt
						this.child = new PolyRechenobjekt(polyRechenobjekt);
					}
				}
				
				// Wiederhole Vorgang rekursiv (durch vorsortierung kann dies in der Rekursion geschehen)
				polyRechenobjekt = polyRechenobjekt.getChild();
			}
		}

		/**
		 * Subtrahiert das polyRechenobjekt von dem aktuellen Polyrechenobjekt (zerst�rt das mitgegebene Objekt)
		 */
		public void sub(PolyRechenobjekt polyRechenobjekt) {
			
			// Dreht Vorzeichen aller Faktoren um bevor diese addiert werden
			while (polyRechenobjekt != null) {
				this.add(new PolyRechenobjekt(polyRechenobjekt.getFaktor() * (-1), polyRechenobjekt.getPotenz()));
				polyRechenobjekt = polyRechenobjekt.getChild();
			}
		}
		
		/**
		 * Multipliziert das polyRechenobjekt mit dem aktuelle Polyrechenobjekt (zerst�rt das mitgegebene Objekt)
		 */
		public void mult(PolyRechenobjekt polyRechenobjekt) {
			
			this.innerMult(polyRechenobjekt);
			
			// Sortieren und Zusammenfassen
			this.bucketSort();
		}
		
		/**
		 * Dividiert das polyRechenobjekt mit dem aktuelle Polyrechenobjekt (zerst�rt das mitgegebene Objekt)
		 */
		public void div(PolyRechenobjekt polyRechenobjekt) {
			
			// TODO TV implementieren
		}
		
		/**
		 * Sortiert die Rechenobjekte und f�gt Rechenobjekte mit der gleichen Potenz zusammen
		 */
		private void bucketSort() {
			
			// Buckets initialisieren
			List<PolyRechenobjekt>[] buckets = new List[this.potenzValue + 1];
			
			// Streuen
			PolyRechenobjekt tempPolyRo = this.clone();
			while(tempPolyRo != null) {
				int bucketIndex = tempPolyRo.getPotenz();
				if(buckets[bucketIndex] == null) {
					buckets[bucketIndex] = List.of(new PolyRechenobjekt(tempPolyRo));
				}
				else {
					buckets[bucketIndex].add(new PolyRechenobjekt(tempPolyRo));
				}
				
				tempPolyRo = tempPolyRo.getChild();
			}
			
			// Sammeln (von niedrig nach hoch um Vergleiche zu minimieren)
			tempPolyRo = new PolyRechenobjekt(0, 0);
			for(List<PolyRechenobjekt> bucket:
				buckets) {
				if(bucket != null) {
					for(PolyRechenobjekt polyRo:
						bucket) {
						
						// addiert keine �berfl�ssigen Objekte
						if(polyRo.getFaktor() != 0) {
							tempPolyRo.add(polyRo);
						}
					}
				}
			}
			
			// Speichern
			this.faktorValue = tempPolyRo.getFaktor();
			this.potenzValue = tempPolyRo.getPotenz();
			this.child = tempPolyRo.getChild();
		}
		
		private void innerMult(PolyRechenobjekt polyRechenobjekt) {
			
			// F�hrt die Multiplikation rekursiv auf jedes Child aus
			if (this.child != null) {
				this.child.innerMult(polyRechenobjekt);
			}
			
			PolyRechenobjekt ausgabeObjekt = new PolyRechenobjekt(0, 0);
			
			// Multipliziert jedes Rechenobjekt des eingegebenen Objekts mit "this"
			// Das direkte hinzuf�gen von 
			while (polyRechenobjekt != null) {
				ausgabeObjekt.add(new PolyRechenobjekt(this.faktorValue * polyRechenobjekt.getFaktor(), this.potenzValue + polyRechenobjekt.getPotenz()));
				
				polyRechenobjekt = polyRechenobjekt.getChild();
			}
			
			// TODO TV Ausgabe garantiert sortiert?
			ausgabeObjekt.add(this.child);
			this.faktorValue = ausgabeObjekt.getFaktor();
			this.potenzValue = ausgabeObjekt.getPotenz();
			this.child = ausgabeObjekt.getChild();
		}

		/**
		 * Potenziert das polyRechenobjekt mit dem aktuelle Polyrechenobjekt (zerst�rt das mitgegebene Ojekt)
		 */
		public void pot(PolyRechenobjekt potenzPolyRechenobjekt) {
			// TODO TV abfangen, wenn x hoch != 0 oder !� N
			
			PolyRechenobjekt clonedPolyRo = this.clone();
			this.potenzValue = 0;
			int potenz = (int)potenzPolyRechenobjekt.getFaktor();
			for(int i = 0; i < potenz; i++) {
				this.innerMult(clonedPolyRo);
			}
			
			// Sortieren und Zusammenfassen
			this.bucketSort();
		}
		
		/**
		 * Erstellt ein neues PolyRechenobjekt mit den gleichen Werten
		 */
		@Override
		public PolyRechenobjekt clone() {
			if(this.child == null) {
				return new PolyRechenobjekt(this);
			}
			else {
				return new PolyRechenobjekt(this.faktorValue, this.potenzValue, this.child.clone());
			}
		}
		
		/**
		 * Gibt Horner Schema mit fehlenden Klammern am Anfang aus
		 */
		@Override
		public String toString() {
			if(this.child != null) {
				return this.faktorValue + ")x + " + this.child.toString();
			}
			
			return this.faktorValue + "";
		}
	}
}
