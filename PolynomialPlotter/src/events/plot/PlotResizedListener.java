package events.plot;

import java.awt.event.ComponentListener;

import logic.BusinessLogic;
import view.GUI;

import java.awt.event.ComponentEvent;

public class PlotResizedListener implements ComponentListener{

    // Noch nicht im View Implementiert
    private BusinessLogic logic;
    private GUI view;

    public PlotResizedListener(GUI view, BusinessLogic logic){
        this.view = view;
        this.logic = logic;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // On Resize --> Funktion neu berechnen mit neuen Dimensionen
        System.out.println("EVENT TRIGGERED: Plotter Resized");
        logic.resize();
        
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // Vielleicht für die Zukunfs nice
        
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // ""
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // """
        
    }
    
}
