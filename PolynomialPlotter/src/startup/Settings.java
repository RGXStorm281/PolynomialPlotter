/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;

import model.ISettings;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.*;

/**
 *
 * @author robinepple
 */
public class Settings implements ISettings {
    
	//Logging.
	private final Logger LOGGER;
	
    // Plot.
    private final int INITIAL_PLOT_WIDTH;
    private final int INITIAL_PLOT_HEIGHT;
    private final int INITIAL_PIXEL_TO_UNIT_RATIO;
    private final int SUB_SQUARE_GRID;
    private final int SQUARE_SCALE;
    private final String THEME;
    
    // Standardposition.
    private final int INITIAL_ORIGIN_X;
    private final int INITIAL_ORIGIN_Y;
    
    // Berechnung der Funktionswerte.
    private final int CALCULATE_EVERY_X_PIXELS;
    private final int THREADPOOL_SIZE;

    public Settings(String path, Logger logger) throws FileNotFoundException, IOException {
        this.LOGGER = logger;
        
        Properties propertiesFile = new Properties();
        propertiesFile.load(new FileReader(path));

        this.INITIAL_PLOT_WIDTH = Integer.parseInt(propertiesFile.getProperty("initialPlotWidth"));
        this.INITIAL_PLOT_HEIGHT = Integer.parseInt(propertiesFile.getProperty("initialPlotHeight"));
        this.INITIAL_PIXEL_TO_UNIT_RATIO = Integer.parseInt(propertiesFile.getProperty("pixelToUnitRatio"));
        this.SUB_SQUARE_GRID = Integer.parseInt(propertiesFile.getProperty("subSquareGrid"));
        this.SQUARE_SCALE = Integer.parseInt(propertiesFile.getProperty("squareScale"));
        this.THEME = propertiesFile.getProperty("theme");

        this.INITIAL_ORIGIN_X = Integer.parseInt(propertiesFile.getProperty("initialOriginX"));
        this.INITIAL_ORIGIN_Y= Integer.parseInt(propertiesFile.getProperty("initialOriginY"));

        this.CALCULATE_EVERY_X_PIXELS = Integer.parseInt(propertiesFile.getProperty("calculateEveryXPixels"));
        this.THREADPOOL_SIZE = Integer.parseInt(propertiesFile.getProperty("threadpoolSize"));
    }    

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public int getInitialPlotWidth() {
        return INITIAL_PLOT_WIDTH;
    }

    @Override
    public int getInitialPlotHeight() {
        return INITIAL_PLOT_HEIGHT;
    }

    @Override
    public int getInitialPixelToUnitRatio() {
        return INITIAL_PIXEL_TO_UNIT_RATIO;
    }

    @Override
    public int getSubSquareGrid() {
        return SUB_SQUARE_GRID;
    }

    @Override
    public int getSquareScale() {
        return SQUARE_SCALE;
    }

    @Override
    public String getTheme() {
        return THEME;
    }

    @Override
    public int getInitialOriginX() {
        return INITIAL_ORIGIN_X;
    }

    @Override
    public int getInitialOriginY() {
        return INITIAL_ORIGIN_Y;
    }

    @Override
    public int getCalculateEveryXPixels() {
        return CALCULATE_EVERY_X_PIXELS;
    }

    @Override
    public int getThreadpoolSize() {
        return THREADPOOL_SIZE;
    }
}
