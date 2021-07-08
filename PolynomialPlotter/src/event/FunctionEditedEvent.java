package event;

import java.awt.Color;

public class FunctionEditedEvent extends FunctionEvent {
    char oldFunctionChar;

    /**
     * Child von FunctioEvent
     * trägt einen Parameter mehr: oldFunctionChar. dieser wird als Identifikation der originalen Funktion verwendet.
     * 
     * @param source         Object das dieses Event gecalled hat
     * @param functionColor  Farbe der Funktion
     * @param functionString Funktions-String
     * @param functionName   Funktions Name/Buchstabe/Identifier [f,g,h,...]
     * @param oldFunctionChar Alter Funktions-Name (falls sich dieser geändert hat, als identifikation)
     */
    public FunctionEditedEvent(Object source, Color functionColor, String functionString, Character functionName,
            char oldFunctionChar) {
        super(source, functionColor, functionString, functionName);
        this.oldFunctionChar = oldFunctionChar;
    }

    public char getOldFunctionChar() {
        return this.oldFunctionChar;
    }

}
