package event;

import model.Tuple;

public class PlotZoomedEvent extends PlotEvent{

    float zoomAmount;
    Tuple<Integer,Integer> mousePosition;

    public PlotZoomedEvent(Object source, int width, int height, float zoomAmount, Tuple<Integer,Integer> mousePosition) {
        super(source, width, height);
        this.zoomAmount = zoomAmount;
        this.mousePosition = mousePosition;
    }

    
    /** Gibt die zoomAmount des Events zurück
     * @return float
     */
    public float getZoomAmount(){
        return this.zoomAmount;
    }

    /**
     * Gibt die Mausposition zurück, an der gezoomed wurde.
     * @return 
     */
    public Tuple<Integer, Integer> getMousePosition() {
        return mousePosition;
    }
}
