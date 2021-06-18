package event;
public class PlotEvent extends java.util.EventObject{

    int plotWidth;
    int plotHeight;

    public PlotEvent(Object source,int width, int height) {
        super(source);
        this.plotWidth = width;
        this.plotHeight = height;

    }

    public int getPlotWidth(){
        return this.plotWidth;
    }

    public int getPlotHeight(){
        return this.plotHeight;
    }


}
