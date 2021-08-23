/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;

/**
 * Abstrakte Oberklasse für Standardfunktionen aller Funktionstypen.
 *
 * @author robinepple
 */
public abstract class Function implements IFunction {

    private boolean visible;
    protected String displayString;
    protected String functionName;
    protected Color functionColor;

    /**
     * Basiskonstruktor für die Erstellung einer Funktion mit zufälliger Farbe.
     *
     * @param functionName Der Name der Funktion.
     * @param displayString Der Funktionsterm, so wie er auf der GUI angezeigt
     * werden soll.
     */
    public Function(String functionName, String displayString) {
        // Per default ist die Funktion sichtbar.
        this.visible = true;
        this.functionName = functionName;
        this.displayString = displayString;
        this.functionColor = randomColor();
    }

    /**
     * Basiskonstruktor für die Erstellung einer Funktion mit expliziter Farbe.
     *
     * @param functionName Der Name der Funktion.
     * @param displayString Der Funktionsterm, so wie er auf der GUI angezeigt
     * werden soll.
     * @param functionColor Die Farbe, in der der Funktionsgraph gezeichnet
     * werden soll.
     */
    public Function(String functionName, String displayString, Color functionColor) {
        // Per default ist die Funktion sichtbar.
        this.visible = true;
        this.functionName = functionName;
        this.displayString = displayString;
        this.functionColor = functionColor;
    }

    /**
     * generiert eine zufällge Farbe.
     *
     * @return Color
     */
    private Color randomColor() {
        return new Color((int) (Math.random() * 256d), (int) (Math.random() * 256d), (int) (Math.random() * 256d));
    }

    /**
     * @ihneritDoc
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @inheritDoc
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @inheritDoc
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * @inheritDoc
     */
    public String getDisplayString() {
        return displayString;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setColor(Color color) {
        functionColor = color;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Color getColor() {
        return functionColor;
    }
}
