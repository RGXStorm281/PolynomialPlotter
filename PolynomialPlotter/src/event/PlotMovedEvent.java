package event;

import model.Koordinate;

public class PlotMovedEvent extends PlotEvent{

    Koordinate dist;
    public PlotMovedEvent(Object source, int width, int height, Koordinate dist) {
        super(source, width, height);
        this.dist = dist;
    }

    public Koordinate getDist(){
        return this.dist;
    }
    
}
