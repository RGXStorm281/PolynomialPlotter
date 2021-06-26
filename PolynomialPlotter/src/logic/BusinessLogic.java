/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.awt.Color;
import java.awt.Point;
import startup.Settings;
import java.util.ArrayList;
import model.DrawingInformationContainer;
import model.FunctionInfoContainer;
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
    private double intervallSizeX;
    private double intervallSizeY;
    private double step;
    
    private Point mousePoint;
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
    	
    }
    
    public void deleteFunction(){
        
    }
    
    public void setMousePoint(Point mousePoint){
        this.mousePoint = mousePoint;
    }
    
    public Point getMousePoint(){
        return mousePoint;
    }
    
    /**
     * Bewegt die Position des sichtbaren Intervalls und zeichnet neu.
     * Achtung: X und Y werden positiv addiert. Wenn mit der Maus "gezogen" wird, dann muss hier der Gegenvektor zum "Ziehen" mitgegeben werden.
     * @param dx Die X-Komponente des Verschiebungsvektors.
     * @param dy Die Y-Komponente des Verschiebungsvektors.
     */
    public void moveCanvas(float dx, float dy){
        positionX += dx;
        positionY += dy;
        debugPosition();
        
        calculateAndDraw();
    }
    
    /**
     * Aktualisiert die Intervallgröße zur Anpassung an die Fenstergröße und zeichnet neu.
     */
    public void resize(){
        var newCanvasWidth = gui.getPlotWidth();
        var newCanvasHeight = gui.getPlotHeight();
        
        if(newCanvasWidth == 0 || newCanvasHeight == 0){
            return;
        }
        
        intervallSizeX *= (double)newCanvasWidth / cachedCanvasWidth;
        intervallSizeY *= (double)newCanvasHeight / cachedCanvasHeight;
        debugIntervallSize();
        
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
        intervallSizeX *= 1 + scale;
        intervallSizeY *= 1 + scale;
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
        double halfInterval = intervallSizeX / 2;
        double intervallStart = positionX - halfInterval;
        double intervallEnd = positionX + halfInterval;
        
        ArrayList<IFunction> functions = functionManager.getFunctionList();
        int numberOfFunctions = functions.size();
        
        FunctionInfoContainer[] functionInfo = new FunctionInfoContainer[numberOfFunctions];
        for(int i = 0; i < numberOfFunctions; i++){
            var currentFunction = functions.get(i);
            var functionValues = currentFunction.calculate(intervallStart, intervallEnd, step);
            functionInfo[i] = new FunctionInfoContainer(currentFunction, functionValues);
        }
        
        var drawingInformation = new DrawingInformationContainer(functionInfo, intervallStart, intervallEnd, step);
        gui.drawFunctions(drawingInformation);
    }
}
