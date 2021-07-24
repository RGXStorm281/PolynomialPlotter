/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

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
        
        if(functionName != null){
            System.out.println("New function \""+functionName+"\" with: "+functionString+" and the Color"+functionColor);
        }else{
            System.out.println("New function \""+functionName+"\" with: "+functionString+" and the Color"+functionColor);
        }
        
        functionName = logic.addFunction(functionName, functionString, functionColor);
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
    public void functionDeleted(FunctionEvent e) {
        System.out.println("Event Triggered: Function Delete \""+e.getFunctionString()+"\"");
    }

    @Override
    public void functionVisibilityToggled(FunctionVisibilityToggledEvent e) {
        String functionName = e.getFunctionName();
        JFunctionComponent jfc = (JFunctionComponent)e.getSource();
        jfc.setVisibility(!jfc.getVisibility());
        logic.toggleFunctionVisible(functionName);
    }
    
}
