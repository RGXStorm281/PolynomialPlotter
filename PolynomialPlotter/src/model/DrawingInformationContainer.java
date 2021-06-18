/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author robinepple
 */
public class DrawingInformationContainer {
    
    // Koordinatensystem und Positionsinformationen sind der GUI bekannt.
    // -> Koordinatensystem Zeichen braucht keine Weiteren Informationen.
    
    // Benötigt werden nur die Informationen zum Zeichnen der Funktionen.
    
    private FunctionInfoContainer[] functionInfo;
    
    private double intervallStart;
    private double intervallEnd;
    private double step;

    public DrawingInformationContainer(FunctionInfoContainer[] functionInfo, double intervallStart, double intervallEnd, double step) {
        this.functionInfo = functionInfo;
        this.intervallStart = intervallStart;
        this.intervallEnd = intervallEnd;
        this.step = step;
    }

    /**
     * Gibt die Informationen über die Funktionen zurück.
     */
    public FunctionInfoContainer[] getFunctionInfo() {
        return functionInfo;
    }

    /**
     * Der errechnete Startwert des Intervalls.
     */
    public double getIntervallStart() {
        return intervallStart;
    }

    /**
     * Der errechnete Endwert des Intervalls.
     */
    public double getIntervallEnd() {
        return intervallEnd;
    }

    /**
     * Die Schrittweite für die einzelnen Werte.
     */
    public double getStep() {
        return step;
    }
    
}
