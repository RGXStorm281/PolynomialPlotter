/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.DrawingInformationContainer;


import event.IFunctionListener;
import event.IPlotListener;
import model.IFunction;

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
     * Aktualisiert die Funktionsliste.
     * @param functions Die Funktionen.
     */
    public void updateFunctionList(IFunction[] functions);

    /**
     * Fügt einen FunctionListener hinzu
     * @param functionListener
     */
    void addFunctionListener(IFunctionListener functionListener);

    /**
     * Fügt einen PlotListener hinzu
     * @param plotListener
     */
    void addPlotListener(IPlotListener plotListener);
    
    int getPlotWidth();

    int getPlotHeight();


    public JPlotter getPlotter();

    public void updateTheme();

    public void start();
}
