/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import event.FunctionEditedEvent;
import event.FunctionEvent;
import event.IFunctionListener;
import java.awt.Color;
import view.IGUI;

/**
 *
 * @author robinepple
 */
public class FunctionListener implements IFunctionListener {
    
    private IGUI view;
    
    public FunctionListener(IGUI view){
        this.view = view;
    }
    
    @Override
    public boolean functionAdded(FunctionEvent e) {
        String functionString = e.getFunctionString();
        Color functionColor = e.getFunctionColor();    
        char functionChar = e.getFunctionChar();
        System.out.println("New function \""+functionChar+"\" with: "+functionString+" and the Color"+functionColor);
        view.addJFunctionComponent(functionChar,functionString, functionColor);
        view.updateTheme();
        return true; // Return ob gegebene funktion legal war

    }

    @Override
    public boolean functionEdited(FunctionEditedEvent e) {
        System.out.println(e.getOldFunctionChar()+"-Function was edited to: "+e.getFunctionString()+" with the color: "+e.getFunctionColor());
        return true; // Return ob der Edit einen Fehler verursach / Legal war
    }

    @Override
    public void functionDeleted(FunctionEvent e) {
        System.out.println("Event Triggered: Function Delete \""+e.getFunctionString()+"\"");
    }
    
}
