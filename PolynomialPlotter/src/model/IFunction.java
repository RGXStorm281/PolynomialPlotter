/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;

import logic.FunctionParsingException;

public interface IFunction {
    
    /**
     * Berechnet einen Funktionswert zum angegebenen X-Wert.
     * @param xValue Der X-Wert.
     * @return Die Koordinate, bestehend aus X- und Y-Wert.
     */
    public Koordinate calculate(double xValue);
	
	/**
	 * Berechnet die Ableitung der Function, sofern möglich.
	 * @return Ableitung.
	 * @throws Exception
	 */
	public IFunction getAbleitung() throws FunctionParsingException;
    
    /**
     * Setzt die Farbe der Funktion.
     * @param color Die neue Farbe.
     */
    public void setColor(Color color);
    
    /**
     * Gibt die Farbe der Funktion zurück.
     */
    public Color getColor();
    
    /**
     * Setzt das Sichtbarkeitsflag der Funktion.
     * @param visible Die Sichtbarkeit.
     */
    public void setVisible(boolean visible);
    
    /**
     * Gibt zurück, ob der Graph der Funktion auf der GUI sichtbar sein soll.
     * @return 
     */
    public boolean isVisible();
    
}
