package event;

import java.awt.Color;
/**
 * 
 * @author lukas
 *
 */
public class FunctionDerivedEvent extends FunctionEvent {
	
	String oldFunctionChar;
	
	/**
     * Event f�r die Ableitung einer Funktion.
     * trägt einen Parameter mehr: oldFunctionChar. dieser wird als Identifikation der originalen Funktion verwendet.
     * 
     * @param source         Object das dieses Event gecalled hat
     * @param functionColor  Farbe der Funktion
     * @param functionString Funktions-String
     * @param functionName   Funktions Name/Buchstabe/Identifier [f,g,h,...]
     * @param oldFunctionChar Alter Funktions-Name (da sich dieser beim Ableiten �ndern soll (hinzuf�gen von Apostroph))
     */
	public FunctionDerivedEvent(Object source, Color functionColor, String functionString, String functionName, String oldFunctionChar) {
		super(source, functionColor, functionString, functionName);
		this.oldFunctionChar = oldFunctionChar;
	}

}
