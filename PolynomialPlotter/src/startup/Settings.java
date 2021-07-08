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
    
    // Plot.
    public final int INITIAL_PLOT_WIDTH;
    public final int INITIAL_PLOT_HEIGHT;
    public final int INITIAL_PIXEL_TO_UNIT_RATIO;
    public final int SUB_SQUARE_GRID;
    public final String THEME;
    
    // Standardposition.
    public final int INITIAL_ORIGIN_X;
    public final int INITIAL_ORIGIN_Y;
    
    // Einstellungen für den Zoom.
    public final double INITIAL_ZOOM;
    public final double MIN_ZOOM;
    public final double MAX_ZOOM;
    public final float STANDARD_ZOOM_SCALE;
    public final float INCREASED_ZOOM_SCALE;
    
    // Berechnung der Funktionswerte.
    public final int CALCULATE_EVERY_X_PIXELS;
    public final int THREADPOOL_SIZE;

    public Settings(String path) throws FileNotFoundException, IOException {
        Properties propertiesFile = new Properties();
        propertiesFile.load(new FileReader(path));

        this.INITIAL_PLOT_WIDTH = Integer.parseInt(propertiesFile.getProperty("initialPlotWidth"));
        this.INITIAL_PLOT_HEIGHT = Integer.parseInt(propertiesFile.getProperty("initialPlotHeight"));
        this.INITIAL_PIXEL_TO_UNIT_RATIO = Integer.parseInt(propertiesFile.getProperty("pixelToUnitRatio"));
        this.SUB_SQUARE_GRID = Integer.parseInt(propertiesFile.getProperty("subSquareGrid"));
        this.THEME = propertiesFile.getProperty("theme");

        this.INITIAL_ORIGIN_X = Integer.parseInt(propertiesFile.getProperty("initialOriginX"));
        this.INITIAL_ORIGIN_Y= Integer.parseInt(propertiesFile.getProperty("initialOriginY"));

        this.INITIAL_ZOOM = Double.parseDouble(propertiesFile.getProperty("initialZoom"));
        this.MIN_ZOOM = Double.parseDouble(propertiesFile.getProperty("minZoom"));
        this.MAX_ZOOM = Double.parseDouble(propertiesFile.getProperty("maxZoom"));
        this.STANDARD_ZOOM_SCALE = Float.parseFloat(propertiesFile.getProperty("standardZoomScale"));
        this.INCREASED_ZOOM_SCALE = Float.parseFloat(propertiesFile.getProperty("increasedZoomScale"));

        this.CALCULATE_EVERY_X_PIXELS = Integer.parseInt(propertiesFile.getProperty("calculateEveryXPixels"));
        this.THREADPOOL_SIZE = Integer.parseInt(propertiesFile.getProperty("threadpoolSize"));
    }

    
}
