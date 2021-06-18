/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;

/**
 *
 * @author robinepple
 */
public class FunctionInfoContainer {
    
    // Darstellungsfarbe der Linie
    private IFunction function;
    
    // Funktionswerte: Item1 = X-Wert, Item2 = Y-Wert
    private Koordinate[] functionValues;

    public FunctionInfoContainer(IFunction function, Koordinate[] functionValues) {
        this.function = function;
        this.functionValues = functionValues;
    }

    /**
     * Gibt die Definition der Funktion zurück, die Informationen zu Farbe, Name, ... hält.
     * @return 
     */
    public IFunction getFunction() {
        return function;
    }
    
    /**
     * Gibt die Funktionswerte zurück.
     * Item1 = X-Wert, Item2 = Y-Wert
     */
    public Koordinate[] getFunctionValues() {
        return functionValues;
    }
}
