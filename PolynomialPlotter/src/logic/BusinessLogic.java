/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import event.PlotEvent;
import java.awt.Point;
import startup.Settings;
import java.util.ArrayList;
import model.DrawingInformationContainer;
import model.FunctionInfoContainer;
import model.IFunction;
import model.Tuple;
import model.Utils;
import view.IGUI;

/**
 *
 * @author robinepple
 */
public class BusinessLogic {
    
    private final IGUI gui;
    private final FunctionManager functionManager;
    private final Settings settings;
    
    private double positionX;
    private double positionY;
    private double intervallSizeX;
    private double intervallSizeY;
    private double step;
    
    private int cachedCanvasHeight;
    private int cachedCanvasWidth;

    public BusinessLogic(IGUI gui, FunctionManager functionManager, Settings settings) {
        this.gui = gui;
        this.functionManager = functionManager;
        this.settings = settings;
        
        initPositon(settings);
    }
    
    /**
     * Initialisisiert die Positionsdaten.
     * @param settings Die App-Settings.
     */
    private void initPositon(Settings settings){
        // Startposition (0|0).
        this.positionX = settings.INITIAL_ORIGIN_X;
        this.positionY = settings.INITIAL_ORIGIN_Y;
        
        // Für eventuelles Resizing zwischenspeichern.
        this.cachedCanvasWidth = settings.INITIAL_PLOT_WIDTH;
        this.cachedCanvasHeight = settings.INITIAL_PLOT_HEIGHT;
        
        // Ab Start sichtbare Intervalle in X-/Y-Richtung.
        this.intervallSizeX = cachedCanvasWidth / settings.INITIAL_PIXEL_TO_UNIT_RATIO;
        this.intervallSizeY = cachedCanvasHeight / settings.INITIAL_PIXEL_TO_UNIT_RATIO;
        
        evaluateStep();
    }
    
    /**
     * Berechnet die Schrittweite der Funktions-Wertetabelle, ausgehend von intervallSizeX und der gecacheten Bildbreite.
     */
    private void evaluateStep(){
        double numberOfCalculations = (double)cachedCanvasWidth / settings.CALCULATE_EVERY_X_PIXELS;
        this.step = intervallSizeX / numberOfCalculations;
    }
    
    public void addFunction(String function){
        functionManager.parseAndAddFunction(function);
    }
    
    public void deleteFunction(){
        
    }
    
    /**
     * Bewegt die Position des sichtbaren Intervalls und zeichnet neu.Achtung: X und Y werden positiv addiert.Wenn mit der Maus "gezogen" wird, dann muss hier der Gegenvektor zum "Ziehen" mitgegeben werden.
     * @param distance Die Distanz in Pixeln, um die das Feld bewegt werden soll.
     */
    public void moveCanvas(Tuple<Integer,Integer> distance){
        double moveX = Utils.mapToInterval(distance.getItem1(), 0, cachedCanvasWidth, 0, intervallSizeX);
        double moveY = Utils.mapToInterval(distance.getItem2(), 0, cachedCanvasHeight, 0, intervallSizeY);
        positionX += moveX;
        positionY += moveY;
        debugPosition();
        
        calculateAndDraw();
    }
    
    /**
     * Aktualisiert die Intervallgröße zur Anpassung an die Fenstergröße und zeichnet neu.
     * @param e Das Event, das die Plot Informationen enthält.
     */
    public void resize(PlotEvent e){
        var newCanvasWidth = e.getPlotWidth();
        var newCanvasHeight = e.getPlotHeight();
        
        if(newCanvasWidth == 0 || newCanvasHeight == 0){
            return;
        }
        
        // Intervalle mit der Panelgröße skalieren.
        intervallSizeX *= (double)newCanvasWidth / cachedCanvasWidth;
        intervallSizeY *= (double)newCanvasHeight / cachedCanvasHeight;
        debugIntervallSize();
        
        // gecachete Panelgröße aktualisieren.
        cachedCanvasWidth = newCanvasWidth;
        cachedCanvasHeight = newCanvasHeight;
        
        evaluateStep();
        calculateAndDraw();
    }
    
    /**
     * Passt die berechneten Intervalle an, um dem Zoomfaktor gerecht zu werden.
     * @param scale Der Wert, mit dem die Intervalle skaliert werden.
     */
    public void zoom(float scale){
        // Skalierung der X-Achse anpassen.
        intervallSizeX *= 1 + scale;
        // Skalierung auf Y-Achse übertragen.
        intervallSizeY = intervallSizeX * ((double)cachedCanvasHeight / cachedCanvasWidth);
        debugIntervallSize();
        calculateAndDraw();
    }
    
    private void debugIntervallSize(){
        System.out.println("Intervall X: " + intervallSizeX + "; Intervall Y: " + intervallSizeY);
    }
    private void debugPosition(){
        System.out.println("Position X: " + positionX + "; Position Y: " + positionY);
    }
    
    /**
     * Nutzt die aktuellen Positionsdaten um die sichtbaren Funktionswerte zu berechnen und auf der GUI auszugeben.
     */
    private void calculateAndDraw(){
        double halfIntervallX = intervallSizeX / 2;
        Tuple<Double,Double> intervallX = new Tuple<>(positionX - halfIntervallX, positionX + halfIntervallX);
        double halfIntervallY = intervallSizeY / 2;
        Tuple<Double,Double> intervallY = new Tuple<>(positionY - halfIntervallY, positionY + halfIntervallY);
        
        ArrayList<IFunction> functions = functionManager.getFunctionList();
        int numberOfFunctions = functions.size();
        
        FunctionInfoContainer[] functionInfo = new FunctionInfoContainer[numberOfFunctions];
        for(int i = 0; i < numberOfFunctions; i++){
            var currentFunction = functions.get(i);
<<<<<<< HEAD
            var functionValues = currentFunction.calculate(intervallX.getItem1(), intervallX.getItem2(), step);
            functionInfo[i] = new FunctionInfoContainer(currentFunction, functionValues);
=======
//            var functionValues = currentFunction.calculate(intervallStart, intervallEnd, step);
//            functionInfo[i] = new FunctionInfoContainer(currentFunction, functionValues);
>>>>>>> feature/devLE
        }
        
        var drawingInformation = new DrawingInformationContainer(functionInfo, intervallX, intervallY, step);
        gui.drawFunctions(drawingInformation);
    }
}
