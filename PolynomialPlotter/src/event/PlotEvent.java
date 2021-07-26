package event;

/**
 * @author raphaelsack
 */
public class PlotEvent extends java.util.EventObject{

    int plotWidth;
    int plotHeight;

    /**
     * Plot Event wird für jegliches Event-Handling mit dem Plot verwendet
     * es trägt mindestens immer die Source, und die dimensionen des plots
     * @param source Caller des Events
     * @param width momentane Breite des Plots
     * @param height momentane Höhe des Plots
     */
    public PlotEvent(Object source,int width, int height) {
        super(source);
        this.plotWidth = width;
        this.plotHeight = height;

    }

    
    /** Gibt die Plot-Breite zum Zeitpunkt des Event-Calls zurück
     * @return int
     */
    public int getPlotWidth(){
        return this.plotWidth;
    }

    
    /** Gibt die Plot-Höhe zum Zeitpunkt des Event-Calls zurück
     * @return int
     */
    public int getPlotHeight(){
        return this.plotHeight;
    }


}
