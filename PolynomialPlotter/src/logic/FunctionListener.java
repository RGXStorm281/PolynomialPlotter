/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import event.FunctionDerivedEvent;
import event.FunctionEditedEvent;
import event.FunctionEvent;
import event.FunctionVisibilityToggledEvent;
import event.IFunctionListener;
import java.awt.Color;
import view.IGUI;
import view.JFunctionComponent;

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
    public String functionAdded(FunctionEvent e) throws FunctionParsingException {
        String functionString = e.getFunctionString();
        Color functionColor = e.getFunctionColor();    
        String functionName = e.getFunctionName();

        
        functionName = logic.addFunction(functionName, functionString, functionColor);
        if(functionString.split("=").length == 1){
            functionString = functionName+"(x) = "+functionString;
        }
            
        
        view.addJFunctionComponent(functionName,functionString, functionColor);
        view.updateTheme();
        return functionName;
    }

    @Override
    public String functionEdited(FunctionEditedEvent e) throws FunctionParsingException {
        var targetFunctionName = e.getOldFunctionChar();
        var newFunctionName = e.getFunctionName();
        var newFunctionString = e.getFunctionString();
        var functionColor = e.getFunctionColor();
        
        System.out.println(targetFunctionName+"-Function was edited to: "+newFunctionString+" with the color: "+functionColor);
        
        newFunctionName = logic.editFunction(targetFunctionName, newFunctionName, newFunctionString, functionColor);
        return newFunctionName;
    }
    
    @Override
    public String functionDerived(FunctionDerivedEvent e)throws FunctionParsingException {
		//TODO RE implement
    	System.out.println("Function " + e.getFunctionName() + " was derived.");
    	return null;    	
    }

    @Override
    public void functionDeleted(FunctionEvent e) {
        System.out.println("Event Triggered: Function Delete \""+e.getFunctionName()+"\"");
        logic.deleteFunction(e.getFunctionName());
    }

    @Override
    public void functionVisibilityToggled(FunctionVisibilityToggledEvent e) {
        String functionName = e.getFunctionName();
        JFunctionComponent jfc = (JFunctionComponent)e.getSource();
        jfc.setVisibility(!jfc.getVisibility());
        logic.toggleFunctionVisible(functionName);
    }
    
}
