package events.plot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import logic.BusinessLogic;
import view.GUI;

public class PlotMouseDraggedListener implements MouseMotionListener{

    private BusinessLogic logic;
    private GUI view;

    public PlotMouseDraggedListener(GUI view, BusinessLogic logic){
        this.view = view;
        this.logic = logic;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("EVENT TRIGGERED: Plotter Dragged");
        // Bisher gehabt, muss die Logik jetzt implementieren:
        // Bei einem Mausklick (in PlotMouseClicked) einen Punkt "mousePt" auf der Momentanen MausPosition speichern (e.getPoint())
        // float dx = e.getX() - mousePt.x;
        // float dy = e.getY() - mousePt.y;
        // Differenz-Vektor Nehmen
        //
        // Ändere den Ursprung, basierend auf dx,dy
        // origin.setLocation(origin.x + dx, origin.y + dy);
        // Origin entsprich hierbei dem Ursprung, also Koordinate 0,0 und wird je nach Drag verschoben -> Damit kann man die Verschiebung des XIntervalls berechnen 
        // Origin benötigt auch der View!
        // Am Ende den mousePt updaten
        // mousePt = e.getPoint();
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Uninteressant
        
    }
    
}
