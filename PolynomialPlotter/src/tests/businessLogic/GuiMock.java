/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.businessLogic;

import tests.*;
import event.IFunctionListener;
import event.IPlotListener;
import model.DrawingInformationContainer;
import model.IFunction;
import model.ISettings;
import view.IGUI;

/**
 *
 * @author robinepple
 */
public class GuiMock implements IGUI {
    
    private int height;
    private int width;
    
    public DrawingInformationContainer drawingInformation;

    public GuiMock(ISettings settings) {
        this.height = settings.getInitialPlotHeight();
        this.width = settings.getInitialPlotWidth();
    }

    @Override
    public void drawFunctions(DrawingInformationContainer drawingInformation) {
        this.drawingInformation = drawingInformation;
    }

    @Override
    public void updateFunctionList(IFunction[] functions) {
        
    }

    @Override
    public void addFunctionListener(IFunctionListener functionListener) {
        
    }

    @Override
    public void addPlotListener(IPlotListener plotListener) {
        
    }

    @Override
    public int getPlotWidth() {
        return height;
    }

    @Override
    public int getPlotHeight() {
        return width;
    }

    @Override
    public void updateTheme() {
        
    }
    
}
