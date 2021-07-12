/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import event.IPlotListener;
import event.PlotEvent;
import event.PlotMovedEvent;
import event.PlotZoomedEvent;
import model.Tuple;

/**
 *
 * @author robinepple
 */
public class PlotListener implements IPlotListener {
    
    private BusinessLogic logic;

    public PlotListener(BusinessLogic logic) {
        this.logic = logic;
    }
    
    @Override
    public void plotResized(PlotEvent e) {
        logic.resize(e.getPlotWidth(), e.getPlotHeight());

    }

    @Override
    public void plotZoomed(PlotZoomedEvent e) {
        logic.zoom(e.getZoomAmount(), e.getMousePosition());

    }

    @Override
    public void plotMoved(PlotMovedEvent e) {
        Tuple<Integer,Integer> moveDistance = e.getDist();
        logic.moveCanvas(moveDistance);
    }
}
