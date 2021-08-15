/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.businessLogic;
import tests.*;
import java.util.logging.Logger;
import startup.ISettings;

/**
 *
 * @author robinepple
 */
public class SettingsMock implements ISettings{
    
    public Logger logger;
    public int initialPlotWidth;
    public int initialPlotHeight;
    public int initialPixelToUnitRatio;
    public int subSquareGrid;
    public int squareScale;
    public String theme;
    public int initialOriginX;
    public int initialOriginY;
    public int calculateEveryXPixels;
    public int threadpoolSize;

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public int getInitialPlotWidth() {
        return initialPlotWidth;
    }

    @Override
    public int getInitialPlotHeight() {
        return initialPlotWidth;
    }

    @Override
    public int getInitialPixelToUnitRatio() {
        return initialPixelToUnitRatio;
    }

    @Override
    public int getSubSquareGrid() {
        return subSquareGrid;
    }

    @Override
    public int getSquareScale() {
        return squareScale;
    }

    @Override
    public String getTheme() {
        return theme;
    }

    @Override
    public int getInitialOriginX() {
        return initialOriginX;
    }

    @Override
    public int getInitialOriginY() {
        return initialOriginY;
    }

    @Override
    public int getCalculateEveryXPixels() {
        return calculateEveryXPixels;
    }

    @Override
    public int getThreadpoolSize() {
        return threadpoolSize;
    }
    
}
