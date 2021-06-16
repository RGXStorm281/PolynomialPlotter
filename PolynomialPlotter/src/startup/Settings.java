/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author robinepple
 */
public class Settings {
    


    public final int INITIAL_PLOT_WIDTH;
    public final int INITIAL_PLOT_HEIGHT;
    public final int PIXEL_TO_UNIT_RATIO;
    public final int INITIAL_ORIGIN_X;
    public final int INITIAL_ORIGIN_Y;
    public final double INITIAL_ZOOM;
    public final double MIN_ZOOM;
    public final double MAX_ZOOM;

    public Settings(String path) throws FileNotFoundException, IOException {
            Properties propertiesFile = new Properties();
            propertiesFile.load(new FileReader(path));
            this.INITIAL_PLOT_WIDTH = Integer.parseInt(propertiesFile.getProperty("initialPlotWidth"));
            this.INITIAL_PLOT_HEIGHT = Integer.parseInt(propertiesFile.getProperty("initialPlotHeight"));
            this.PIXEL_TO_UNIT_RATIO = Integer.parseInt(propertiesFile.getProperty("pixelToUnitRatio"));
            this.INITIAL_ORIGIN_X = Integer.parseInt(propertiesFile.getProperty("initialOriginX"));
            this.INITIAL_ORIGIN_Y= Integer.parseInt(propertiesFile.getProperty("initialOriginY"));

            this.INITIAL_ZOOM = Double.parseDouble(propertiesFile.getProperty("initialZoom"));
            this.MIN_ZOOM = Double.parseDouble(propertiesFile.getProperty("minZoom"));
            this.MAX_ZOOM = Double.parseDouble(propertiesFile.getProperty("maxZoom"));

    }

    
}
