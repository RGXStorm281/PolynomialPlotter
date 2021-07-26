package event;
/**
 * @author raphaelsack
 */
public interface IPlotListener extends java.util.EventListener{

    /**
     * Wird gecallt wenn der Plot in irgendeiner Form resized wird
     * @param e trägt die dimensions-Daten des Plots
     */
    void plotResized(PlotEvent e);

    /**
     * Wird gecallt wenn auf dem Plot in irgendeiner Form gezoomt wird
     * @param e trägt den zoomfaktor mit sich (dZoom)
     */
    void plotZoomed(PlotZoomedEvent e);

    /**
     * Wird gecallt wenn der Plot in irgendeiner Art Bewegt wird (Im sinne von Draggen mit der maus z.B.)
     * @param e trägt informationen über den distanz-Vektor (wie viel wurde bewegt?)
     */
    void plotMoved(PlotMovedEvent e);
}
