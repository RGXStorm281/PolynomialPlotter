package event;

public class PlotZoomedEvent extends PlotEvent{

    float zoomAmount;

    public PlotZoomedEvent(Object source, int width, int height, float zoomAmount) {
        super(source, width, height);
        this.zoomAmount = zoomAmount;
    }

    public float getZoomAmount(){
        return this.zoomAmount;
    }
    
}
