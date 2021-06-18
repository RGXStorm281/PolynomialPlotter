package event;

public interface PlotListener extends java.util.EventListener{

    void plotResized(PlotEvent e);

    void plotZoomed(PlotZoomedEvent e);

    void plotMoved(PlotMovedEvent e);
}
