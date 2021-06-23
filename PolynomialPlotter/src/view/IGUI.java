/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.DrawingInformationContainer;

import event.FunctionListener;
import event.PlotListener;

import java.awt.Color;

/**
 *
 * @author raphaelsack
 */
public interface IGUI{

    // /**
    //  * Gibt den Start- und Endwert für das zu berechnende Intervall zurück.
    //  */
    // Tuple<Double,Double> getIntervallX();

    // /**
    //  * Gibt die Schrittweite für die Funktions-Wertetabelle zurück.
    //  */
    // double getFunctionStep();
     
    /**
     * Zeichnet die Funktionen mit den gegebenen Funktions-Wertetabellen.
     * @param drawingInformation Die nötigen Informationen für's Zeichnen.
     */
    void drawFunctions(DrawingInformationContainer drawingInformation);

    /**
     * Mit den Funktionen kann ein neuer Listener hinzugefügt werden
     * in diesem Listener werden dann bei jedem Aufruf, das Funktionsobjekt und
     * die Farbe des Graphens geholt und gespeichert werden. 
     */

    /**
     * Gibt die aktuelle Funktions-Eingabe als UniversalFunctionObjekt zurück
     */
    String getFunction();

    /**
     * 
     * @return Farbe der neuen Funktion
     */
    Color getColor();


    /**
     * Fügt einen FunctionListener hinzu
     * @param functionListener
     */
    void addFunctionListener(FunctionListener functionListener);

    /**
     * Fügt einen PlotListener hinzu
     * @param plotListener
     */
    void addPlotListener(PlotListener plotListener);
    
    int getPlotWidth();

    int getPlotHeight();
}
