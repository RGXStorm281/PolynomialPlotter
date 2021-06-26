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
import logic.FunctionListener;
import logic.FunctionManager;
import logic.PlotListener;
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
            view.addFunctionListener(new FunctionListener(view));

            view.addPlotListener(new PlotListener(logic));
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
