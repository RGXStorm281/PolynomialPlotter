package event;

import model.Koordinate;
import model.Tuple;

public class PlotMovedEvent extends PlotEvent{

    Tuple<Double,Double> dist;
    /**
     * Child von PlotEvent, trägt als Extra den Distanz-Vektor mit informationen um wie viele Einheiten der Plot bewegt wurde
     * @param source
     * @param width
     * @param height
     * @param dist Distanz-Vektor der Bewegung
     */
    public PlotMovedEvent(Object source, int width, int height, Tuple<Double,Double> dist) {
        super(source, width, height);
        this.dist = dist;
    }

    
    /** gibt den Distanz-Vektor in Form einer Koordinate zurück
     * @return Koordinate
     */
    public Tuple<Double,Double> getDist(){
        return this.dist;
    }
    
}
