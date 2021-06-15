/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import startup.Settings;
import java.util.ArrayList;
import model.IFunction;
import model.Tuple;
import view.IGUI;

/**
 *
 * @author robinepple
 */
public class BusinessLogic {
    
    private IGUI gui;
    
    private FunctionManager functionManager;
    
    private Settings settings;
    
    private double positionX;
    
    private double positionY;

    public BusinessLogic(IGUI gui, FunctionManager functionManager, Settings settings) {
        this.gui = gui;
        this.functionManager = functionManager;
        this.settings = settings;
        this.positionX = 0;
        this.positionY = 0;
    }
    
    /**
     * Berechnet das relevante Intervall auf der X-Achse zur Anzeige.
     * @return Tupel (double,double) mit Anfang und Ende des Intervals
     */
//    private Tuple evaluateIntervallXFromVisibleArea(){
//        int width = gui.getPlotWidth();
//        int widthScale  = width / settings.plotDefaultWidth;
//        float zoom = gui.getPlotZoom();
//        double intervalSizeX = settings.defaultIntervalSizeX * (1/zoom) * widthScale;
//        
//        double leftBorder = positionX + (intervalSizeX/2);
//        double rightBorder = positionX + (intervalSizeX/2);
//        
//        return new Tuple(leftBorder, rightBorder);
//    }
//    
//    /**
//     * Berechnet die Schrittweise für die Berechnung der Funktionen;
//     * @param intervalSize die länge des Intervals, für das die Berechnungen durchgeführt werden.
//     * @return die Schrittweite.
//     */
//    private double evaluateStepSize(double intervalSize){
//        int steps = evaluateNumberOfSteps();
//        return intervalSize / steps;
//    }
//    
//    /**
//     * Berechnet die Anzahl Werte, die jede Funktion berechnen muss;
//     * @return Die Anzahl Schritte.
//     */
//    private int evaluateNumberOfSteps(){
//        int widthInPixels = gui.getPlotWidth();
//        // Wir wollen aus Performance Gründen eventuell nicht jeden Pixel berechnen, sondern Markierungspunkte alle x Pixel und diese dann per Bézer Kurve verbinden.
//        int steps = (int)Math.ceil((double)widthInPixels / settings.defaultCalculationPixelJump);
//        return steps;
//    }
//    
//    /**
//     * Berechnet die Funktionswerte für die Darstellung.
//     * @return Zweidimensionales Array mit den Werten für jede Funktion.
//     */
//    public double[][] evaluateFunctionValuesForDisplay(){
//        Tuple interval = evaluateIntervallXFromVisibleArea();
//        double intervalStart = (double)interval.getItem1();
//        double intervalEnd = (double)interval.getItem2();
//        
//        double stepSize = evaluateStepSize(intervalStart - intervalEnd);
//        
//        ArrayList<IFunction> functions = functionManager.getFunctionList();
//        int numberOfFunctions = functions.size();
//        
//        double[][] functionValues = new double[numberOfFunctions][evaluateNumberOfSteps()];
//        
//        for(int i = 0; i < numberOfFunctions; i++){
//            functionValues[i] = functions.get(i).calculate(intervalStart, intervalEnd, stepSize);
//        }
//        
//        return functionValues;
//    }
}
