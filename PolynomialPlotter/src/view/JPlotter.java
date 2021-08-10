package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;



import event.PlotEvent;
import event.PlotMovedEvent;
import event.PlotZoomedEvent;
import model.DrawingInformationContainer;
import model.FunctionInfoContainer;
import model.IFunction;
import model.Koordinate;
import startup.Settings;
import view.GUI.FontFamily;
import view.GUI.FontStyle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import event.IPlotListener;
import model.Tuple;
import model.Utils;

/**
 * @author raphaelsack
 */

public class JPlotter extends JPanel {


    private float squareScale; // Die Skalierung der Quadrate 
    private float squareDivider; // Der Wert wie die Quadrate skaliert werden
    private int xAxisValueYOffset = 10;
    private int yAxisValueXOffset = 10;
    private float smallSquareStrokeWidth = 0.5f;
    private float bigSquareStrokeWidth = 2f;
    private float axisStrokeWidth = 2.5f;
    private Color plotBackground;
    private Color plotForeground;
    private StyleClass styleClass;
    private Settings settings;

    private List<IPlotListener> plotListeners = new ArrayList<IPlotListener>();
    private Point mousePt; // Point to keep track of the last mouse-position


    private int subGrid; // Wie viele kleine Quadrate soll ein Großes Quadrat auf dem Grid haben

    private Color gridColor;
    // Temporäre Vars

    private DrawingInformationContainer drawingInformation;

    public JPlotter(Settings _settings, StyleClass styleClass) {
        this.styleClass = styleClass;
        this.settings = _settings;
        this.subGrid = _settings.SUB_SQUARE_GRID;
        this.plotBackground = styleClass.PLOT_BG;
        this.gridColor = styleClass.GIRD_COLOR;
        this.plotForeground = styleClass.PLOT_FG;
        this.squareDivider = _settings.SQUARE_SCALE;
        this.squareScale = _settings.SQUARE_SCALE;
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                float dZoom = e.isControlDown() ? 0.1f : 0.05f;
                dZoom*=e.getWheelRotation();
                for(IPlotListener listener: plotListeners)listener.plotZoomed(new PlotZoomedEvent(e.getSource(), getWidth(), getHeight(), dZoom, new Tuple<Integer,Integer>(e.getX(), e.getY())));
            }
        });
        // Mouse Listener für die Drag-Funktionalität und um den Cursor zu ändern
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                repaint();
            }

           
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }

        });
        // MouseMotionListener für die Drag-Funktionalität
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
                // Differenz zwischen der momentanten und letzten Maus-Position
                int dx = mousePt.x - e.getX();
                int dy = mousePt.y - e.getY();
                // Ändere den Ursprung, basierend auf dx,dy
                for(IPlotListener listener: plotListeners)listener.plotMoved(new PlotMovedEvent(e.getSource(), getWidth(), getHeight(), new Tuple<Integer,Integer>(dx,dy)));
                mousePt = e.getPoint();
                repaint();
            }
        });

        addComponentListener(new ComponentListener(){

            @Override
            public void componentResized(ComponentEvent e) {
                for(IPlotListener listener: plotListeners)listener.plotResized(new PlotEvent(e.getSource(), getWidth(), getHeight()));
            }

            @Override
            public void componentMoved(ComponentEvent e) {                
            }

            @Override
            public void componentShown(ComponentEvent e) {     
            }

            @Override
            public void componentHidden(ComponentEvent e) {    
            }
            
        });
        setPreferredSize(new Dimension(_settings.INITIAL_PLOT_WIDTH,_settings.INITIAL_PLOT_HEIGHT));
    }

    /**
     * Beim ändern der Funktionsinformationen wird der Plot neu gezeichnet
     * @param drawingInformation 
     */
    protected void updateDrawingInformation(DrawingInformationContainer drawingInformation){
        this.drawingInformation = drawingInformation;
        repaint();
    }

    
    /** 
     * @param g
     */
    public void paintComponent(Graphics g) {                                                             
        Graphics2D g2d = (Graphics2D) g;
        // Antialiasing für bessere Diagonalen
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Hintergrund-Füllung
        g2d.setColor(this.plotBackground);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if(drawingInformation != null){
            drawGrid(g2d);
            drawAxes(g2d);
            drawValues(g2d);
            drawFunctions(g2d);
        }
    }

    /**
     * Malt die Zahlen auf den X/Y-Achsen
     * @param g2d
     */
    private void drawValues(Graphics2D g2d) {
        Tuple<Double,Double> xInterval = drawingInformation.getIntervallX();
        Tuple<Double,Double> yInterval = drawingInformation.getIntervallY();
        Tuple<Integer,Integer> heightInterval = new Tuple<Integer,Integer>(0, getHeight());
        Tuple<Integer,Integer> widthInterval = new Tuple<Integer,Integer>(0, getWidth());
        Font font = GUI.getFont(15);
        g2d.setPaint(plotForeground);
        g2d.setFont(font);
        int stringHeight = (int) font.createGlyphVector(g2d.getFontRenderContext(), "1").getVisualBounds().getHeight(); // Höhe ist immer gleich, wird am anfang berechnet

        // Die 0-Achsen Punkte werden zuerst auf Höhe/Breite gemappt und danach wird über die Clamp-Funktion geschaut, ob diese Koordinate "sichtbar" ist, 
        // Die Clamp funktion nimmt einen EingabeWert und ein Interval als Input, falls das entsprechende Intervall "sichtbar" ist, wird der EingabeWert zurückgegeben
        // Wenn der Eingabewert kleiner ist als der Intervalstart, dann wird der Intervalstart zurückgegeben
        // Wenn der Eingabewert größer ist als das Intervalende, dann wird der Intervalende zurückgegeben
        // Somit ist die Koordiante der Achse entweder die Wirkliche Koordiante, oder der Rand des Plotters
        int xAxis = Utils.clampToDimensions(Utils.mapToInterval(0, yInterval, heightInterval),heightInterval); // Y-Koordinate der x-Achse
        int yAxis = Utils.clampToDimensions(Utils.mapToInterval(0, xInterval, widthInterval),widthInterval); // X-Koordinat der y-Achse

        /**
         * Starte unten auf der Y-Achse und runde den Wert auf einen Sinnvollen und passenden Wert runter
         * z.B. das Y-Interval wäre (-4.35,4.35) und squareScale wäre noch auf 1. Dann würde man rechnen -4.35 - 0.35 - 1 = 3. der extra Offset wird benötigt um einen Visuellen Bug zu verhindern
         * die Schleife geht dann von diesem Intervall-Start los und fügt i immer einmal den Wert von squareScale hinzu und zwar so lange bis das Ende des Intervalls erreicht ist plus dem Selben offset wie beim Anfang
         * Wäre beim Selben interval, squareScale 2.0, dann würde die Schleife bei -2.0 Anfangen und bei jedem Schritt kommen 2 dazu bis man 6 erreicht hat
         * Bei squareScale 0.5 wären es die Werte -4.5,-4,-3.5...
         */
        for(double i = (yInterval.getItem1()-yInterval.getItem1()%squareScale-squareScale);i<yInterval.getItem2()+squareScale;i+=squareScale){
            if(i == 0)continue; // Zeichne nie die 0
            int y = Utils.mapToDimension(i, yInterval,heightInterval); // Mappe i auf die Y-Achse (Wenn die Achse z.B. 100 Pixel hoch ist, das Interval von 1-10 geht und i = 5 dann ist y = 50)
            int yOff = (int) (stringHeight/2); // In der Höhe zentrieren
            int xOff = (int) (g2d.getFontMetrics().stringWidth(i+"")); // In der Breite zentrieren

            // Wenn die Y-Achse nicht sichtbart ist, dann zeichne den String einfach mit einem Offset an die Seite des Canvas
            // Da yAxisValueXOffset einmal für den Offset vom Rand und als Offset zur Achse verwendet wird, müssen wir den Offset in der überprüfung mal 2 nehmen.
            // Wenn die y-Achse sichtbar ist, dann zeichne den String einfach mit einem Offset relativ zur Y-Achse^
            g2d.drawString(-i+"", yAxis <= yAxisValueXOffset*2+xOff? yAxisValueXOffset : yAxis-xOff-yAxisValueXOffset, y+yOff); 
        }
        // Gleiche wie oben nur mit der X-Achse
        for(double i = (xInterval.getItem1()-xInterval.getItem1()%squareScale-squareScale);i<xInterval.getItem2()+squareScale;i+=squareScale){
            if(i == 0)continue;
            int x = Utils.mapToDimension(i, xInterval,widthInterval);
            int xOff = (int) (g2d.getFontMetrics().stringWidth(i+"")/2);
            g2d.drawString(i+"", x-xOff, xAxis >= getHeight()-stringHeight-xAxisValueYOffset*2 ? getHeight()-xAxisValueYOffset : xAxis+stringHeight+xAxisValueYOffset);
        }
        
    }

    /** 
     * @param g2d
     */
    private void drawFunctions(Graphics2D g2d) {
        Tuple<Double,Double> xInterval = drawingInformation.getIntervallX();
        Tuple<Double,Double> yInterval = drawingInformation.getIntervallY();
        Tuple<Double,Double> heightInterval = new Tuple<Double,Double>((double)0, (double)getHeight());
        Tuple<Double,Double> widthInterval = new Tuple<Double,Double>((double)0, (double)getWidth());
        FunctionInfoContainer[] functionInfo = drawingInformation.getFunctionInfo();
        for(FunctionInfoContainer functionContainer: functionInfo){
            IFunction func = functionContainer.getFunction();
            // ausgeblendete Funktionen überspringen.
            if(!func.isVisible()){
                continue;
            }
            
            g2d.setColor(func.getColor());
            Koordinate[] values = functionContainer.getFunctionValues();
            if(values.length == 0){
                continue;
            }
            GeneralPath gp = new GeneralPath();
            gp.moveTo(Utils.mapToInterval(values[0].getX(), xInterval, widthInterval),Utils.mapToInterval(-values[0].getY(), yInterval, heightInterval));
            for(int i = 1;i<values.length;i++){
                if(i+1 < values.length && values[i].distanceTo(values[i+1])>20)continue; // Für Pole mit VZW
                gp.lineTo(Utils.mapToInterval(values[i].getX(), xInterval, widthInterval),Utils.mapToInterval(-values[i].getY(), yInterval, heightInterval));
            }
            g2d.draw(gp);
        }

    }

    /**
     * Zeichnet eine Mathematische Funktion basierend auf den Momentanen
     * Transformationen auf den Plotter
     * 
     * @param g2d      Graphics2D context
     * @param function Lambda Funktion
     * @param color    Farbe der Linie
     * @deprecated Nur eine Test-Funktion für Dimensionen, passt nicht in die
     *             BusinessLogic
     */
    
    /**
     * Zeichnet die X und Y Achsen auf den Bildschirm
     * 
     * @param g2d Graphics2D context
     */
    private void drawAxes(Graphics2D g2d) {
        Tuple<Double,Double> xInterval = drawingInformation.getIntervallX();
        Tuple<Double,Double> yInterval = drawingInformation.getIntervallY();
        Tuple<Integer,Integer> heightInterval = new Tuple<Integer,Integer>(0, getHeight());
        Tuple<Integer,Integer> widthInterval = new Tuple<Integer,Integer>(0, getWidth());
        g2d.setPaint(plotForeground);
        g2d.setFont(GUI.getFont(15));
        g2d.setStroke(new BasicStroke(axisStrokeWidth));
        // Wenn Achse sichtbar ist, dann zeichne sie
        if(xInterval.getItem1()<=0 && xInterval.getItem2() >=0){
            g2d.setStroke(new BasicStroke(axisStrokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
            int x = (int)Utils.mapToInterval(0, xInterval, widthInterval);
            g2d.drawLine(x, 0, x, heightInterval.getItem2());
            g2d.drawLine(x, 0, x-5, 10);
            g2d.drawLine(x, 0, x+5, 10);
            g2d.drawString("y", x+20, 20);
        }
        
        if(yInterval.getItem1()<=0 && yInterval.getItem2() >=0){
            g2d.setStroke(new BasicStroke(axisStrokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
            int y = (int)Utils.mapToInterval(0, yInterval, heightInterval);
            g2d.drawLine(0,y,widthInterval.getItem2(),y);
            g2d.drawLine(getWidth(),y,getWidth()-10,y+5);
            g2d.drawLine(getWidth(),y,getWidth()-10,y-5);
            g2d.drawString("x", getWidth()-20, y-20);
        }
        
    }



    /**
     * Zeichnet ein Karierten Hintergrund, basierend auf JPlotter.origin und
     * JPlotter.zoom
     * 
     * @param g2d Graphics2D context
     */
    public void drawGrid(Graphics2D g2d) {
        g2d.setPaint(this.gridColor);
        Tuple<Double,Double> intervallYTuple = drawingInformation.getIntervallY();
        Tuple<Double,Double> intervallXTuple = drawingInformation.getIntervallX();
        Tuple<Double,Double> heightInterval = new Tuple<Double,Double>(0d, (double)getHeight());
        Tuple<Double,Double> widthInterval = new Tuple<Double,Double>(0d, (double)getWidth());
        int nSquares = (int) (Math.abs(intervallXTuple.getItem2()-intervallXTuple.getItem1())/squareScale); // so viele große Quadrate befinden sich in einer Reihe
        
        // Verschiedene Grenzen für die Anzahl an Quadraten in einer Reihe. Bei Fensterbreite von 1000, sind 10 Quadrate nicht viel, wenn die Bildschirmbreite aber nur 100 Pixel ist, dann sind 10 Quadrate zu viel.
        int upper;
        int lower;
        if(getWidth() >= 750){
            upper = 25;
            lower = 10;
        }else if(getWidth()>=500){
            upper = 20;
            lower = 5;
        }else if(getWidth()>=400){
            upper = 15;
            lower = 5;
        }else{
            upper = 5;
            lower = 1;
        }
        if(nSquares>=upper){ // Wenn zu viele in einer Reihe sind
            squareScale*=squareDivider; // Squares "mergen"
        }else if(nSquares<=lower){ // Wenn es zu wenig sind
            squareScale/=squareDivider; // Squares aufteilen
        }
        int substep = (int) Math.round(getHeight()/Math.abs(intervallYTuple.getItem2()-intervallYTuple.getItem1())/subGrid*squareScale);

        // Für i, welches bei der ersten ganzen Zahl die zur Scale passt startet [Bsp.: 3.141 - (3.141%1) - 1 = 2] minus squareScale um eventuelle ansichtsfehler zu vermeiden
        // i geht bis zum ende des intervalls plus squareScale
        // i wächst jedesmal um so viel, wie die nächste linie 
        for(double i = (intervallYTuple.getItem1()-intervallYTuple.getItem1()%squareScale-squareScale);i<intervallYTuple.getItem2()+squareScale;i+=squareScale){
            g2d.setStroke(new BasicStroke(bigSquareStrokeWidth));
            int y = (int) Utils.mapToInterval(i, intervallYTuple,heightInterval);
            // Zeichne die linie
            g2d.drawLine(0, y, getWidth(),y);
            g2d.setStroke(new BasicStroke(smallSquareStrokeWidth));
            // Zeichne die kleinen Linien 
            for(int j = 0;j<subGrid;j++){
                g2d.drawLine(0, (int)(y+substep*j), getWidth(),(int)(y+substep*j) );
            }
        }
        for(double i = (intervallXTuple.getItem1()-intervallXTuple.getItem1()%squareScale-squareScale);i<intervallXTuple.getItem2()+squareScale;i+=squareScale){
            g2d.setStroke(new BasicStroke(bigSquareStrokeWidth));
            int x = (int) Utils.mapToInterval(i, intervallXTuple,widthInterval);
            g2d.drawLine(x,0,x,getHeight());
            g2d.setStroke(new BasicStroke(smallSquareStrokeWidth));
            for(int j = 0;j<subGrid;j++){
                g2d.drawLine((int)(x+substep*j),0,(int)(x+substep*j),getHeight());
            }
        }

        
    }
    
    /** Fügt einen PlotListener an das Event an
     * @param plotListener
     */
    public void addPlotListener(IPlotListener plotListener) {
        plotListeners.add(plotListener);
    }

    public void recolor() {
        this.plotBackground = styleClass.PLOT_BG;
        this.gridColor = styleClass.GIRD_COLOR;
        this.plotForeground = styleClass.PLOT_FG;
        repaint();
    }
}
