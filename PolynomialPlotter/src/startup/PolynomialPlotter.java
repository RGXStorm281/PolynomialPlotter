/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import java.awt.Color;

import event.*;
import logic.BusinessLogic;
import logic.FunctionManager;
import model.Koordinate;
import view.GUI;

/**
 *
 * @author robinepple
 */
public class PolynomialPlotter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Objekte initialisieren
            Settings settings = new Settings("src/startup/settings.properties");
            FunctionManager model = new FunctionManager();
            GUI view = new GUI(settings);
            BusinessLogic logic = new BusinessLogic(view, model, settings);

            // Events Setzen
            view.addFunctionListener(new FunctionListener(){

                @Override
                public boolean functionAdded(FunctionEvent e) {
                    String functionString = e.getFunctionString();
                    Color functionColor = e.getFunctionColor();    
                    char functionChar = e.getFunctionChar();
                    System.out.println("New function \""+functionChar+"\" with: "+functionString+" and the Color"+functionColor);

                    return true; // Return ob gegebene funktion legal war
                    
                }

                @Override
                public boolean functionEdited(FunctionEditedEvent e) {
                    System.out.println(e.getOldFunctionChar()+"-Function was edited to: "+e.getFunctionString()+" with the color: "+e.getFunctionColor());
                    return true; // Return ob der Edit einen Fehler verursach / Legal war
                }

                @Override
                public void functionDeleted(FunctionEvent e) {
                    System.out.println("Event Triggered: Function Delete \""+e.getFunctionString()+"\"");
                }
                
            });

            view.addPlotListener(new PlotListener(){

                @Override
                public void plotResized(PlotEvent e) {
                    logic.resize();
                    
                }

                @Override
                public void plotZoomed(PlotZoomedEvent e) {
                    logic.zoom(e.getZoomAmount());
                    
                }

                @Override
                public void plotMoved(PlotMovedEvent e) {
                    Koordinate moveDistance = e.getDist();
                    System.out.println(moveDistance);
                }
                
            });
            // Applikation starten
            SwingUtilities.invokeLater(new Runnable(){
                @Override 
                public void run(){
                    view.start();
                }
            });
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PolynomialPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PolynomialPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
