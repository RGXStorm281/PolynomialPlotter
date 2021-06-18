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
    private Color lineColor;
    
    // Funktionswerte: Item1 = X-Wert, Item2 = Y-Wert
    private Tuple<Double,Double>[] functionValues;

    public FunctionInfoContainer(Color lineColor, Tuple<Double, Double>[] functionValues) {
        this.lineColor = lineColor;
        this.functionValues = functionValues;
    }

    /**
     * Gibt die Farbe zurück, in der die Linie des Graphen gezeichnet werden soll.
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Gibt die Funktionswerte zurück.
     * Item1 = X-Wert, Item2 = Y-Wert
     */
    public Tuple<Double, Double>[] getFunctionValues() {
        return functionValues;
    }
}
