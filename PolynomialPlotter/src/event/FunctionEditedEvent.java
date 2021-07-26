package event;

import java.awt.Color;
/**
 * @author raphaelsack
 */
public class FunctionEditedEvent extends FunctionEvent {
    String oldFunctionChar;

    /**
     * Child von FunctionEvent
     * trägt einen Parameter mehr: oldFunctionChar. dieser wird als Identifikation der originalen Funktion verwendet.
     * 
     * @param source         Object das dieses Event gecalled hat
     * @param functionColor  Farbe der Funktion
     * @param functionString Funktions-String
     * @param functionName   Funktions Name/Buchstabe/Identifier [f,g,h,...]
     * @param oldFunctionChar Alter Funktions-Name (falls sich dieser geändert hat, als identifikation)
     */
    public FunctionEditedEvent(Object source, Color functionColor, String functionString, String functionName,
            String oldFunctionChar) {
        super(source, functionColor, functionString, functionName);
        this.oldFunctionChar = oldFunctionChar;
    }

    public String getOldFunctionChar() {
        return this.oldFunctionChar;
    }

}
