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
    
    private Tuple<Double,Double> intervallX;
    private Tuple<Double,Double> intervallY;
    private double step;

    public DrawingInformationContainer(FunctionInfoContainer[] functionInfo, Tuple<Double, Double> intervallX, Tuple<Double, Double> intervallY, double step) {
        this.functionInfo = functionInfo;
        this.intervallX = intervallX;
        this.intervallY = intervallY;
        this.step = step;
    }

    public FunctionInfoContainer[] getFunctionInfo() {
        return functionInfo;
    }

    public Tuple<Double, Double> getIntervallX() {
        return intervallX;
    }

    public Tuple<Double, Double> getIntervallY() {
        return intervallY;
    }

    public double getStep() {
        return step;
    }
    
    
}
