/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Abstrakte Oberklasse für Standardfunktionen aller Funktionstypen.
 * @author robinepple
 */
public abstract class Function {
    
    private boolean visible;
    protected String displayString;
    protected String functionName;

    public Function(String functionName, String displayString) {
        // Per default ist die Funktion sichtbar.
        this.visible = true;
        this.functionName = functionName;
        this.displayString = displayString;
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
    public String getFunctionName(){
        return functionName;
    }
    
    /**
     * @inheritDoc
     */
    public String getDisplayString(){
        return displayString;
    }
}
