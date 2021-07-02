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
    private BusinessLogic logic;
    
    public FunctionListener(IGUI view, BusinessLogic logic){
        this.view = view;
        this.logic = logic;
    }
    
    @Override
    public void functionAdded(FunctionEvent e) throws FunctionParsingException {
        String functionString = e.getFunctionString();
        Color functionColor = e.getFunctionColor();    
        char functionChar = e.getFunctionChar();
        System.out.println("New function \""+functionChar+"\" with: "+functionString+" and the Color"+functionColor);
        logic.addFunction(functionChar, functionString, functionColor);
        view.addJFunctionComponent(functionChar,functionString, functionColor);
        view.updateTheme();

    }

    @Override
    public void functionEdited(FunctionEditedEvent e) throws FunctionParsingException {
        var targetFunctionName = e.getOldFunctionChar();
        var newFunctionName = e.getFunctionChar();
        var newFunctionString = e.getFunctionString();
        var functionColor = e.getFunctionColor();
        System.out.println(targetFunctionName+"-Function was edited to: "+newFunctionString+" with the color: "+functionColor);
        logic.editFunction(targetFunctionName, newFunctionName, newFunctionString, functionColor);
    }

    @Override
    public void functionDeleted(FunctionEvent e) {
        System.out.println("Event Triggered: Function Delete \""+e.getFunctionString()+"\"");
    }
    
}
