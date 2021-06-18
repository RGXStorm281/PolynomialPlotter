package events.plot;

import java.awt.event.MouseWheelListener;

import logic.BusinessLogic;
import view.GUI;

import java.awt.event.MouseWheelEvent;

public class PlotZoomedListener implements MouseWheelListener{

    private BusinessLogic logic;
    private GUI view;

    public PlotZoomedListener(GUI view, BusinessLogic logic){
        this.view = view;
        this.logic = logic;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // delta-Zoom
        float dZoom = e.isControlDown() ? 0.1f : 0.05f;
        dZoom*=e.getWheelRotation();
        System.out.println("EVENT TRIGGERED: Plotter Zoomed");
        logic.zoom(dZoom);
    }

    
    
}
