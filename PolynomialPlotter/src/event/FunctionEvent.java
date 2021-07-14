package event;
import java.awt.Color;
public class FunctionEvent extends java.util.EventObject{

    Color functionColor;
    String functionString;
    String functionName;
    /**
     * FunctionEvent wird als Event genutzt für jegliches EventHandling mit Funktionen
     * Sprich: Funktionen adden, löschen oder ändern
     * @param source         Event-Caller
     * @param functionColor  Farbe der Funktion
     * @param functionString Funktions-String
     * @param functionName   Funktions Name/Buchstabe/Identifier [f,g,h,...]
     */
    public FunctionEvent(Object source, Color functionColor, String functionString, String functionName) {
        super(source);
        this.functionColor = functionColor;
        this.functionString = functionString;
        this.functionName = functionName;
    }

    
    /** gibt die Funktions-Farbe zurück
     * @return Color
     */
    public Color getFunctionColor(){
        return this.functionColor;
    }
    
    
    /** gibt den Funktions-String zurück
     * @return String
     */
    public String getFunctionString(){
        return this.functionString;
    }
    
    
    /** Gibt den Funktions-Identifier/Namen zurück [f,g,h,...]
     * @return char
     */
    public String getFunctionName(){
        return this.functionName;
    }
}
