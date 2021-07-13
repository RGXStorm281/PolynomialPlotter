/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import event.PlotEvent;
import java.awt.Color;
import startup.Settings;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import model.DrawingInformationContainer;
import model.FunctionInfoContainer;
import model.IFunction;
import model.Koordinate;
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
    
    private final ExecutorService functionCalculationPool;

    public BusinessLogic(IGUI gui, FunctionManager functionManager, Settings settings) {
        this.gui = gui;
        this.functionManager = functionManager;
        this.settings = settings;
        this.functionCalculationPool = Executors.newFixedThreadPool(settings.THREADPOOL_SIZE);
        
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
    
    /**
     * Versucht, die neue Funktion zu parsen.Wenn das Parsen fehl schlägt liefert die Exception Details über das Problem.
     * @param functionName Der Name der Funktion.
     * @param functionString Ein String, der zur Funktion geparsed wird.
     * @param lineColor Die Farbe, in der die Funktionslinie gezeichnet werden soll.
     * @return Der Funktionsname, der für die angelegte Funktion vergeben wurde.
     * @throws FunctionParsingException Details zum Parsing Fehler.
     */
    public String addFunction(String functionName, String functionString, Color lineColor) throws FunctionParsingException{
        // TODO RE Farbe weitergeben.
        var name = functionManager.parseAndAddFunction(functionName, functionString, lineColor);
        calculateAndDraw();
        return name;
    }
    
    /**
     * Versucht, die neue Funktion zu parsen und die bestehende damit zu ersetzen.Falls das Parsen fehl schlägt wird die bestehende Funktion wiederhergestellt und der Fehler weiter geworfen.
     * @param targetFunctionName Der Name der Funktion, die geändert/überschrieben werden soll.
     * @param newFunctionName  Der neue Name der Funktion.
     * @param newFunctionString Der String, der als neue Funktion geparsed werden soll.
     * @param newLineColor Die Linienfarbe der Funktion.
     * @return Der Funktionsname, den die editierte Funktion nach der Bearbeitung besitzt.
     * @throws FunctionParsingException Details zum Parsing Fehler.
     */
    public String editFunction(String targetFunctionName, String newFunctionName, String newFunctionString, Color newLineColor) throws FunctionParsingException{
        var targetFunction = functionManager.getFunction(targetFunctionName);
        
        // Darf eigentlich nicht vorkommen. Wenn doch: Nichts tun.
        if(targetFunction == null){
            throw new FunctionParsingException(ParsingResponseCode.TargetFunctionEmpty, "Es muss ein Target-Funktionsname angegeben werden.");
        }
        
        // Platz machen, falls sich der Funktionsname nicht geändert hat. 
        functionManager.delete(targetFunctionName);
        
        try {
            // Versuche neue Funktion zu speichern.
            // TODO RE Farbe weitergeben.
            var name = functionManager.parseAndAddFunction(newFunctionName, newFunctionString, newLineColor);
            // Neu berechnen und Zeichnen.
            calculateAndDraw();
            return name;
        }catch (FunctionParsingException f){
            // Falls parsen fehl schlägt bestehende Funktion wiederherstellen.
            functionManager.add(targetFunctionName, targetFunction);
            // Dann Fehler an die GUI weitergeben, damit Problemdetails an den Bentzer weiter gegeben werden können.
            throw f;
        }
    }
    
    /**
     * Löscht die Funktion, falls vorhanden.
     * @param functionName Der Caracter, mit der die Funktion bezeichnet ist.
     */
    public void deleteFunction(String functionName){
        functionManager.delete(functionName);
        calculateAndDraw();
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
     * @param newCanvasWidth neue Breite in Pixeln
     * @param newCanvasHeight neue Höhe in Pixeln
     */
    public void resize(int newCanvasWidth, int newCanvasHeight){
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
        
        calculateAndDraw();
    }
    
    /**
     * Passt die berechneten Intervalle an, um dem Zoomfaktor gerecht zu werden.
     * @param scale Der Wert, mit dem die Intervalle skaliert werden.
     */
    public void zoom(float scale, Tuple<Integer,Integer> zoomOnPixel){
        
        // Skalierung der Achsen anpassen.
        intervallSizeX *= 1 + scale;        
        // Skalierung auf Y-Achse übertragen.
        intervallSizeY = intervallSizeX * ((double)cachedCanvasHeight / cachedCanvasWidth);
        
        // Vom Zentrum auf den Zoompunkt.
        double offsetX = (double)zoomOnPixel.getItem1() - ((double)cachedCanvasWidth / 2);
        double offsetY = (double)zoomOnPixel.getItem2() - ((double)cachedCanvasHeight / 2);
        
        // Vektor mitskalieren.
        double newOffsetX = offsetX / (1 + scale);
        double newOffsetY = offsetY / (1 + scale);
        
        // um den Überhang verschieben:
        int moveX = (int)Math.round(offsetX - newOffsetX);
        int moveY = (int)Math.round(offsetY - newOffsetY);
                
        debugIntervallSize();
        moveCanvas(new Tuple<Integer,Integer>(-moveX, -moveY));
    }
    
    private void debugIntervallSize(){
        // System.out.println("Intervall X: " + intervallSizeX + "; Intervall Y: " + intervallSizeY);
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
        
        FunctionInfoContainer[] functionInfo;
        
        try {
            functionInfo = calculateFunctionValues(intervallX);
        } catch (InterruptedException | ExecutionException ex) {
            // TODO RE Errorhandling (Logging).
            functionInfo = new FunctionInfoContainer[0];
        }
        
        var drawingInformation = new DrawingInformationContainer(functionInfo, intervallX, intervallY, step);
        gui.drawFunctions(drawingInformation);
    }
    
    // Berechnet die Funktionswerte multithreaded.
    private FunctionInfoContainer[] calculateFunctionValues(Tuple<Double,Double> intervallX) throws InterruptedException, ExecutionException{
        // Funktionen laden.
        ArrayList<IFunction> functions = functionManager.getFunctionList();
        int numberOfFunctions = functions.size();
        
        // Die Schrittweite berechnen.
        evaluateStep();
        // Berechnungswerte vorbereiten.
        double intervallStart = intervallX.getItem1();
        int numberOfCalculations = (int)Math.floor(intervallSizeX / step);
        
        var xValues = new double[numberOfCalculations];
        for(int i = 0; i < numberOfCalculations; i++){
            xValues[i] = intervallStart + (i * step);
        }
        
        // Berechnungsthreads starten.
        var futureValues = new Future[numberOfFunctions][numberOfCalculations];
        for(int i = 0; i < numberOfFunctions; i++){
            var currentFunction = functions.get(i);
            for(int j = 0; j < numberOfCalculations; j++){
                var calculator = new FunctionCalculationThread(currentFunction, xValues[j]);
                futureValues[i][j] = functionCalculationPool.submit(calculator);
            }
        }
        
        // Ergebnisse einsammeln.
        var functionValues = new Koordinate[numberOfFunctions][numberOfCalculations];
        for(int i = 0; i < numberOfFunctions; i++){
            for(int j = 0; j < numberOfCalculations; j++){
                functionValues[i][j] = (Koordinate)futureValues[i][j].get();
            }
        }
        
        // Für die Rückgabe verpacken.
        FunctionInfoContainer[] functionInfo = new FunctionInfoContainer[numberOfFunctions];
        for(int i = 0; i < numberOfFunctions; i++){
            var currentFunction = functions.get(i);
            functionInfo[i] = new FunctionInfoContainer(currentFunction, functionValues[i]);
        }
        
        return functionInfo;
    }
}
