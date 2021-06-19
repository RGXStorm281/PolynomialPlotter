package event;

import model.Koordinate;

public class PlotMovedEvent extends PlotEvent{

    Koordinate dist;
    /**
     * Child von PlotEvent, trägt als Extra den Distanz-Vektor mit informationen um wie viele Einheiten der Plot bewegt wurde
     * @param source
     * @param width
     * @param height
     * @param dist Distanz-Vektor der Bewegung
     */
    public PlotMovedEvent(Object source, int width, int height, Koordinate dist) {
        super(source, width, height);
        this.dist = dist;
    }

    
    /** gibt den Distanz-Vektor in Form einer Koordinate zurück
     * @return Koordinate
     */
    public Koordinate getDist(){
        return this.dist;
    }
    
}
