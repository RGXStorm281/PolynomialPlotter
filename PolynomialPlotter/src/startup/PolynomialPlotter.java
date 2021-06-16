/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import events.FunctionAdded;
import view.FunctionDialog;
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
            // Settings
            Settings settings = new Settings("PolynomialPlotter/src/startup/settings.properties");
            GUI gui = new GUI(settings);
            gui.addInputListener(new FunctionAdded(gui));
            SwingUtilities.invokeLater(new Runnable(){
                @Override 
                public void run(){
                    gui.start();
                }
            });
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PolynomialPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PolynomialPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
