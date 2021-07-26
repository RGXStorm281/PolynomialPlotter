/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;

import javax.swing.SwingUtilities;

import logic.BusinessLogic;
import logic.FunctionListener;
import logic.FunctionManager;
import logic.HornerParser;
import logic.IParser;
import logic.PlotListener;
import logic.UniversalParser;
import view.GUI;

/**
 *
 * @author robinepple
 * @author all
 */
public class PolynomialPlotter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Vorbereitungen
            IParser[] availableParsers = new IParser[] {
    		new HornerParser(),
    		new UniversalParser()
            };
            
            // Objekte initialisieren
            Settings settings = new Settings("src/startup/settings.properties");
            FunctionManager model = new FunctionManager(availableParsers);
            GUI view = new GUI(settings);
            BusinessLogic logic = new BusinessLogic(view, model, settings);
            initLogger();

            // Events Setzen
            view.addFunctionListener(new FunctionListener(view, logic));

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
    
    private static void initLogger(){
    	Settings.LOGGER = Logger.getGlobal();
        FileHandler handler = null;
		try {
			handler = new FileHandler("logs/logs%g.log", 50000, 2, true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Settings.LOGGER.addHandler(handler);
    }

}
