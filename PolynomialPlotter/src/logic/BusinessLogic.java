/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.awt.Color;
import startup.Settings;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import model.DrawingInformationContainer;
import model.FunctionInfoContainer;
import model.IFunction;
import model.Koordinate;
import model.Tuple;
import model.Utils;
import startup.ISettings;
import view.IGUI;

/**
 *
 * @author robinepple
 */
public class BusinessLogic {

    private final IGUI gui;
    private final FunctionManager functionManager;
    private final ISettings settings;
    private final Logger logger;

    private double positionX;
    private double positionY;
    private double intervallSizeX;
    private double intervallSizeY;
    private double step;

    private int cachedCanvasHeight;
    private int cachedCanvasWidth;

    private final ExecutorService functionCalculationPool;

    public BusinessLogic(IGUI gui, FunctionManager functionManager, ISettings settings) {
        this.gui = gui;
        this.functionManager = functionManager;
        this.settings = settings;
        this.functionCalculationPool = Executors.newFixedThreadPool(settings.getThreadpoolSize());
        this.logger = settings.getLogger();

        initPositon(settings);
    }

    /**
     * Initialisisiert die Positionsdaten.
     *
     * @param settings Die App-Settings.
     */
    private void initPositon(ISettings settings) {
        // Startposition (0|0).
        this.positionX = settings.getInitialOriginX();
        this.positionY = settings.getInitialOriginY();

        // Für eventuelles Resizing zwischenspeichern.
        this.cachedCanvasWidth = settings.getInitialPlotWidth();
        this.cachedCanvasHeight = settings.getInitialPlotHeight();

        // Ab Start sichtbare Intervalle in X-/Y-Richtung.
        this.intervallSizeX = cachedCanvasWidth / settings.getInitialPixelToUnitRatio();
        this.intervallSizeY = cachedCanvasHeight / settings.getInitialPixelToUnitRatio();

        evaluateStep();
    }

    /**
     * Berechnet die Schrittweite der Funktions-Wertetabelle, ausgehend von
     * intervallSizeX und der gecacheten Bildbreite.
     */
    private void evaluateStep() {
        double numberOfCalculations = (double) cachedCanvasWidth / settings.getCalculateEveryXPixels();
        step = intervallSizeX / numberOfCalculations;
    }

    /**
     * Versucht, die neue Funktion zu parsen.Wenn das Parsen fehl schlägt
     * liefert die Exception Details über das Problem.
     *
     * @param functionName Der Name der Funktion.
     * @param functionString Ein String, der zur Funktion geparsed wird.
     * @param lineColor Die Farbe, in der die Funktionslinie gezeichnet werden
     * soll.
     * @throws FunctionParsingException Details zum Parsing Fehler.
     */
    public void addFunction(String functionName, String functionString, Color lineColor) throws FunctionParsingException {
        var name = functionManager.parseAndAddFunction(functionName, functionString, lineColor);
        logger.info("Neue Funktion " + name + " hinzugefügt.");

        calculateAndDraw();
        updateFunctionList();
    }

    /**
     * Versucht, die neue Funktion zu parsen und die bestehende damit zu
     * ersetzen.Falls das Parsen fehl schlägt wird die bestehende Funktion
     * wiederhergestellt und der Fehler weiter geworfen.
     *
     * @param targetFunctionName Der Name der Funktion, die
     * geändert/überschrieben werden soll.
     * @param newFunctionName Der neue Name der Funktion.
     * @param newFunctionString Der String, der als neue Funktion geparsed
     * werden soll.
     * @param newLineColor Die Linienfarbe der Funktion.
     * @throws FunctionParsingException Details zum Parsing Fehler.
     */
    public void editFunction(String targetFunctionName, String newFunctionName, String newFunctionString, Color newLineColor) throws FunctionParsingException {
        var targetFunction = functionManager.getFunction(targetFunctionName);

        // Darf eigentlich nicht vorkommen. Wenn doch: Nichts tun.
        if (targetFunction == null) {
            throw new FunctionParsingException(ParsingResponseCode.TargetFunctionEmpty, "Target-Funktion nicht gefunden.");
        }

        logger.info("Edit von Funktion " + targetFunctionName + " gestartet...");
        // Platz machen, falls sich der Funktionsname nicht geändert hat. 
        functionManager.delete(targetFunctionName);
        logger.info("... Funktion " + targetFunctionName + " entfernt.");

        try {
            // Versuche neue Funktion zu speichern.
            var name = functionManager.parseAndAddFunction(newFunctionName, newFunctionString, newLineColor);
            logger.info("... neue Funktion " + newFunctionName + " erfolgreich geparsed und eingefügt.");
            // Neu berechnen und Zeichnen.
            calculateAndDraw();
            updateFunctionList();
        } catch (FunctionParsingException f) {
            // Falls parsen fehl schlägt bestehende Funktion wiederherstellen.
            functionManager.add(targetFunctionName, targetFunction);
            logger.info("... parsen fehlgeschlagen, Funktion wurde wiederhergestellt.");
            // Dann Fehler an die GUI weitergeben, damit Problemdetails an den Bentzer weiter gegeben werden können.
            throw f;
        }
    }

    /**
     * Leitet eine Funktion ab, und speichert die Ableitung in einer neuen
     * Funktion.
     *
     * @param functionName Der Name der Funktion, die abgeleitet werden soll.
     * @throws FunctionParsingException
     */
    public void deriveFunction(String functionName) throws FunctionParsingException {
        var targetFunction = functionManager.getFunction(functionName);

        // Darf eigentlich nicht vorkommen. Wenn doch: Nichts tun.
        if (targetFunction == null) {
            throw new FunctionParsingException(ParsingResponseCode.TargetFunctionEmpty, "Target-Funktion nicht gefunden.");
        }

        try {
            logger.info("Die Funktion " + functionName + " wird abgeleitet.");
            var ableitungFunctionName = functionManager.functionAbleitenForFunctionName(functionName);
            logger.info("Die Funktion " + ableitungFunctionName + " ist die Ableitung.");

            calculateAndDraw();
            updateFunctionList();
        } catch (FunctionParsingException f) {
            logger.info("... Ableiten fehlgeschlagen.");

            throw f;
        }
    }

    /**
     * Löscht die Funktion, falls vorhanden.
     *
     * @param functionName Der Caracter, mit der die Funktion bezeichnet ist.
     */
    public void deleteFunction(String functionName) {
        functionManager.delete(functionName);
        logger.info("Funktion " + functionName + " entfernt.");
        calculateAndDraw();
        updateFunctionList();
    }

    /**
     * Schält die Sichtbarkeit einer Funktion um.
     *
     * @param functionName der Name der Funktion.
     */
    public void toggleFunctionVisible(String functionName) {
        functionManager.toggleFunctionVisible(functionName);
        calculateAndDraw();
        updateFunctionList();
    }

    /**
     * Bewegt die Position des sichtbaren Intervalls und zeichnet neu.Achtung: X
     * und Y werden positiv addiert.Wenn mit der Maus "gezogen" wird, dann muss
     * hier der Gegenvektor zum "Ziehen" mitgegeben werden.
     *
     * @param distance Die Distanz in Pixeln, um die das Feld bewegt werden
     * soll.
     */
    public void moveCanvas(Tuple<Integer, Integer> distance) {
        double moveX = Utils.mapToInterval(distance.getItem1(), 0, cachedCanvasWidth, 0, intervallSizeX);
        double moveY = Utils.mapToInterval(distance.getItem2(), 0, cachedCanvasHeight, 0, intervallSizeY);
        positionX += moveX;
        positionY += moveY;
        debugPosition();

        calculateAndDraw();
    }

    /**
     * Aktualisiert die Intervallgröße zur Anpassung an die Fenstergröße und
     * zeichnet neu.
     *
     * @param newCanvasWidth neue Breite in Pixeln
     * @param newCanvasHeight neue Höhe in Pixeln
     */
    public void resize(int newCanvasWidth, int newCanvasHeight) {
        if (newCanvasWidth == 0 || newCanvasHeight == 0) {
            return;
        }

        // Intervalle mit der Panelgröße skalieren.
        intervallSizeX *= (double) newCanvasWidth / cachedCanvasWidth;
        intervallSizeY *= (double) newCanvasHeight / cachedCanvasHeight;
        debugIntervallSize();

        // gecachete Panelgröße aktualisieren.
        cachedCanvasWidth = newCanvasWidth;
        cachedCanvasHeight = newCanvasHeight;

        calculateAndDraw();
    }

    /**
     * Passt die berechneten Intervalle an, um dem Zoomfaktor gerecht zu werden.
     *
     * @param scale Der Wert, mit dem die Intervalle skaliert werden.
     */
    public void zoom(float scale, Tuple<Integer, Integer> zoomOnPixel) {

        // Skalierung der Achsen anpassen.
        intervallSizeX *= 1 + scale;
        // Skalierung auf Y-Achse übertragen.
        intervallSizeY = intervallSizeX * ((double) cachedCanvasHeight / cachedCanvasWidth);

        // Vom Zentrum auf den Zoompunkt.
        double offsetX = (double) zoomOnPixel.getItem1() - ((double) cachedCanvasWidth / 2);
        double offsetY = (double) zoomOnPixel.getItem2() - ((double) cachedCanvasHeight / 2);

        // Vektor mitskalieren.
        double newOffsetX = offsetX / (1 + scale);
        double newOffsetY = offsetY / (1 + scale);

        // um den Überhang verschieben:
        int moveX = (int) Math.round(offsetX - newOffsetX);
        int moveY = (int) Math.round(offsetY - newOffsetY);

        debugIntervallSize();
        moveCanvas(new Tuple<Integer, Integer>(-moveX, -moveY));
    }

    private void debugIntervallSize() {
        // System.out.println("Intervall X: " + intervallSizeX + "; Intervall Y: " + intervallSizeY);
    }

    private void debugPosition() {
        // System.out.println("Position X: " + positionX + "; Position Y: " + positionY);
    }

    /**
     * Aktualisiert die Anzeige der Funktionsliste.
     */
    private void updateFunctionList() {
        var functionList = functionManager.getFunctionList();
        IFunction[] functions = new IFunction[functionList.size()];
        functions = functionList.toArray(functions);
        gui.updateFunctionList(functions);
    }

    /**
     * Nutzt die aktuellen Positionsdaten um die sichtbaren Funktionswerte zu
     * berechnen und auf der GUI auszugeben.
     */
    private void calculateAndDraw() {
        double halfIntervallX = intervallSizeX / 2;
        Tuple<Double, Double> intervallX = new Tuple<>(positionX - halfIntervallX, positionX + halfIntervallX);
        double halfIntervallY = intervallSizeY / 2;
        Tuple<Double, Double> intervallY = new Tuple<>(positionY - halfIntervallY, positionY + halfIntervallY);

        FunctionInfoContainer[] functionInfo;

        try {
            functionInfo = calculateFunctionValues(intervallX);
        } catch (InterruptedException | ExecutionException ex) {
            logger.warning("Fehler bei der Funktionswertberechnung: " + ex.getMessage());
            functionInfo = new FunctionInfoContainer[0];
        }

        var drawingInformation = new DrawingInformationContainer(functionInfo, intervallX, intervallY, step);
        gui.drawFunctions(drawingInformation);
    }

    // Berechnet die Funktionswerte multithreaded.
    private FunctionInfoContainer[] calculateFunctionValues(Tuple<Double, Double> intervallX) throws InterruptedException, ExecutionException {
        // Funktionen laden.
        ArrayList<IFunction> functions = functionManager.getFunctionList();
        int numberOfFunctions = functions.size();

        // Die Schrittweite berechnen.
        evaluateStep();
        // Berechnungswerte vorbereiten.
        double intervallStart = intervallX.getItem1();
        int numberOfCalculations = (int) Math.floor(intervallSizeX / step);

        var xValues = new double[numberOfCalculations];
        for (int i = 0; i < numberOfCalculations; i++) {
            xValues[i] = intervallStart + (i * step);
        }

        // Berechnungsthreads starten.
        var futureValues = new Future[numberOfFunctions][numberOfCalculations];
        for (int i = 0; i < numberOfFunctions; i++) {
            var currentFunction = functions.get(i);
            // Nicht sichtbare Funktionen überspringen.
            if (!currentFunction.isVisible()) {
                continue;
            }
            // Zukünftige Werte speichern.
            for (int j = 0; j < numberOfCalculations; j++) {
                var calculator = new FunctionCalculationThread(currentFunction, xValues[j]);
                futureValues[i][j] = functionCalculationPool.submit(calculator);
            }
        }

        // Ergebnisse einsammeln.
        var functionValues = new Koordinate[numberOfFunctions][numberOfCalculations];
        for (int i = 0; i < numberOfFunctions; i++) {
            var currentFunction = functions.get(i);
            // Nicht sichtbare Funktionen überspringen.
            if (!currentFunction.isVisible()) {
                continue;
            }
            for (int j = 0; j < numberOfCalculations; j++) {
                functionValues[i][j] = (Koordinate) futureValues[i][j].get();
            }
        }

        // Für die Rückgabe verpacken.
        FunctionInfoContainer[] functionInfo = new FunctionInfoContainer[numberOfFunctions];
        for (int i = 0; i < numberOfFunctions; i++) {
            var currentFunction = functions.get(i);
            functionInfo[i] = new FunctionInfoContainer(currentFunction, functionValues[i]);
        }

        return functionInfo;
    }
}
