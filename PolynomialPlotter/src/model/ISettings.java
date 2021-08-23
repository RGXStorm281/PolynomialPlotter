/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.logging.Logger;

/**
 *
 * @author robinepple
 */
public interface ISettings {
    
	//Logging.
	public Logger getLogger();
	
    // Plot.
    public int getInitialPlotWidth();
    public int getInitialPlotHeight();
    public int getInitialPixelToUnitRatio();
    public int getSubSquareGrid();
    public int getSquareScale();
    public String getTheme();
    
    // Standardposition.
    public int getInitialOriginX();
    public int getInitialOriginY();
    
    // Berechnung der Funktionswerte.
    public int getCalculateEveryXPixels();
    public int getThreadpoolSize();
}
