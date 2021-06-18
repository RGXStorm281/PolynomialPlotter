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

    public BusinessLogic(IGUI gui, FunctionManager functionManager, Settings settings) {
        this.gui = gui;
        this.functionManager = functionManager;
        this.settings = settings;
        
        this.positionX = 0;
        this.positionY = 0;
    }
    
    private void initPositon(Settings settings){
        // Startposition (0|0).
        this.positionX = settings.INITIAL_ORIGIN_X;
        this.positionY = settings.INITIAL_ORIGIN_Y;
        
        // Ab Start sichtbare Intervalle in X-/Y-Richtung.
        this.intervallSizeX = settings.INITIAL_PLOT_WIDTH / settings.INITIAL_PIXEL_TO_UNIT_RATIO;
        this.intervallSizeY = settings.INITIAL_PLOT_HEIGHT / settings.INITIAL_PIXEL_TO_UNIT_RATIO;
        
        this.step = evaluateStep(settings.INITIAL_PLOT_WIDTH);
    }
    
    /**
     * Berechnet die Schrittweite der Funktions-Wertetabelle, ausgehend von intervallSizeX.
     * @param screenWidthPixels die aktuelle Breite des Bildes in Pixeln.
     * @return die Schrittweite.
     */
    private double evaluateStep(int screenWidthPixels){
        double numberOfCalculations = Math.ceil((double)screenWidthPixels / settings.CALCULATE_EVERY_X_PIXELS);
        double step = intervallSizeX / numberOfCalculations;
        return step;
    }
    
    
    public void addFunction(String function){
        functionManager.parseAndAddFunction(function);
    }
    
    public void deleteFunction(){
        
    }
    
    
    public void setMousePoint(Point mousePoint){
        this.mousePoint = mousePoint;
    }
    
    public Point getMousePoint(){
        return mousePoint;
    }
    
    public void moveCanvas(float dx, float dy){
        var width = gui.getPlotWidth();
        
    }
    
    public void resize(){
        
    }
    
    public void zoom(float scale){
        
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
