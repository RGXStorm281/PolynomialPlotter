/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.awt.Color;

/**
 *
 * @author robinepple
 */
public class FunctionVisibilityToggledEvent extends FunctionEvent {

    /**
     * Child von FunctioEvent
     * trägt einen Parameter mehr: oldFunctionChar. dieser wird als Identifikation der originalen Funktion verwendet.
     * 
     * @param source         Object das dieses Event gecalled hat
     * @param functionColor  Farbe der Funktion
     * @param functionString Funktions-String
     * @param functionName   Funktions Name/Buchstabe/Identifier [f,g,h,...]
     */
    public FunctionVisibilityToggledEvent(Object source, Color functionColor, String functionString, String functionName) {
        super(source, functionColor, functionString, functionName);
    }
    
}
