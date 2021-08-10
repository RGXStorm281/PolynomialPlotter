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

    public Function() {
        // Per default ist die Funktion sichtbar.
        this.visible = true;
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
}
