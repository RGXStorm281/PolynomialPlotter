/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import view.IGUI;

/**
 *
 * @author robinepple
 */
public class BusinessLogic {
    
    private IGUI gui;
    
    private FunctionManager functionManager;
    
    private double positionX;
    
    private double positionY;

    public BusinessLogic(IGUI gui, FunctionManager functionManager) {
        this.gui = gui;
        this.functionManager = functionManager;
        this.positionX = 0;
        this.positionY = 0;
    }
    
    private void evaluateFunctionValuesForVisibleArea(double step){
        
    }
}
