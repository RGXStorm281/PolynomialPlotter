/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Properties propertiesFile = new Properties();
            Settings settings = new Settings();
            propertiesFile.load(new FileReader("src/logic/settings.properties"));
            settings.defaultCalculationPixelJump = (int) propertiesFile.get("defaultCalculationPixelJump");
            settings.defaultIntervalSizeX = (double) propertiesFile.get("defaultZoomX");
            settings.defaultIntervalSizeY = (double) propertiesFile.get("defaultZoomX");
            settings.plotDefaultWidth = (int) propertiesFile.get("plotDefaultWidth");
            settings.plotDefaultHeight = (int) propertiesFile.get("plotDefaultHeight");
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PolynomialPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PolynomialPlotter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
