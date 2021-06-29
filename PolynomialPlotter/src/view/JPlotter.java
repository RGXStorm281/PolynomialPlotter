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
// import java.awt.BasicStroke;
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

    private double zoom; // Global zoomlevel

    private final float SPACING; // Hardcoded Space-unit, |spacing| pixels = 1 numerical unit
    private float squareScale = 2;
    private int xAxisValueYOffset = 10;
    private int yAxisValueXOffset = 10;

    private List<IPlotListener> plotListeners = new ArrayList<IPlotListener>();
    private Point origin; // Point to keep track of the origin point (Used for dragging the screen)
    private Point mousePt; // Point to keep track of the last mouse-position
    private Settings settings;

    private int subGrid;

    private Color gridColor;
    // Temporäre Vars

    private DrawingInformationContainer drawingInformation;

    public JPlotter(Settings settings, StyleClass styleClass) {
        // Scroll-Listener --> Zoom
        this.settings = settings;
        this.zoom = settings.INITIAL_ZOOM;
        this.SPACING = settings.INITIAL_PIXEL_TO_UNIT_RATIO;
        this.subGrid = settings.SUB_SQUARE_GRID;
        this.gridColor = styleClass.GIRD_COLOR;
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                float dZoom = e.isControlDown() ? 0.1f : 0.05f;
                dZoom*=e.getWheelRotation();
                for(IPlotListener listener: plotListeners)listener.plotZoomed(new PlotZoomedEvent(e.getSource(), getWidth(), getHeight(), dZoom));
            }
        });
        // Mouse Listener für die Drag-Funktionalität und um den Cursor zu ändern
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                repaint();
                requestFocus();
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
                // TODO Auto-generated method stub
                
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        setPreferredSize(new Dimension(settings.INITIAL_PLOT_WIDTH,settings.INITIAL_PLOT_HEIGHT));
    }

    protected void resetZoom() {
        zoom = 1f;
    }

    /**
     * Kümmert sich um die Zoom-Funktionalität, indem es abhängig der
     * Scroll-Richtung den Zoom verändert Hochscrollen = Reinzoomen Runterscrollen =
     * Runterzoomen
     * 
     * @param e
     */
    protected void updateDrawingInformation(DrawingInformationContainer drawingInformation){
        this.drawingInformation = drawingInformation;
        repaint();
    }

    /**
     * Vergrößert den Zoom-Faktor abhängig von dZoom
     * 
     * @param dZoom Um diesen Wert wird reingezoomt
     */
    public void zoomIn(float dZoom) {
        if (dZoom > 0f && this.zoom + dZoom < settings.MAX_ZOOM) {
            this.zoom += dZoom;
        }
    }

    /**
     * Verkleinert den Zoom-Faktor abhängig von dZoom
     * 
     * @param dZoom Um diesen Wert wird rausgezoomt
     */
    public void zoomOut(float dZoom) {
        if (dZoom > 0f && this.zoom - dZoom > settings.MIN_ZOOM) {
            this.zoom -= dZoom;
        }
    }

    
    /** 
     * @param g
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Antialiasing für bessere Diagonalen
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Hintergrund-Füllung
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // g2d.translate((double) origin.x, (double) origin.y); // View abhängig vom Ursprung translaten, wichtig für die
        //                                                      // Drag-Funktionalität
        // g2d.translate(getWidth() / 2, getHeight() / 2); // View auf die Mitte des Panels translaten/
        if(drawingInformation != null){
            drawFunctions(g2d);
            drawGrid(g2d);
            drawAxes(g2d);
            drawValues(g2d);
        }
        // drawFunction(g2d, x -> (float) ((x * x) + 1), Color.RED);
        // drawFunction(g2d, x -> (float) (Math.sin(x) * x * x), Color.BLUE);
    }

    
    private void drawValues(Graphics2D g2d) {
        Tuple<Double,Double> xInterval = drawingInformation.getIntervallX();
        Tuple<Double,Double> yInterval = drawingInformation.getIntervallY();
        Tuple<Integer,Integer> heightInterval = new Tuple<Integer,Integer>(0, getHeight());
        Tuple<Integer,Integer> widthInterval = new Tuple<Integer,Integer>(0, getWidth());
        Font font = GUI.getFont(FontFamily.ROBOTO, FontStyle.REGULAR, 15);
        g2d.setFont(font);
        int stringHeight = (int) font.createGlyphVector(g2d.getFontRenderContext(), "1").getVisualBounds().getHeight();
        int xAxis = Utils.clampToDimensions(Utils.mapToInterval(0, yInterval, heightInterval),heightInterval);
        int yAxis = Utils.clampToDimensions(Utils.mapToInterval(0, xInterval, widthInterval),widthInterval);

        for(double i = (yInterval.getItem1()-yInterval.getItem1()%squareScale-squareScale);i<yInterval.getItem2()+squareScale;i+=squareScale){
            if(i == 0)continue;
            int y = Utils.mapToDimension(i, yInterval,heightInterval);
            int yOff = (int) (stringHeight/2);
            int xOff = (int) (g2d.getFontMetrics().stringWidth(i+""));
            g2d.drawString(i+"", yAxis <= yAxisValueXOffset*2+xOff? yAxisValueXOffset : yAxis-xOff-yAxisValueXOffset, y+yOff);
        }
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
        // Um es wirklich an den canvas anzupassen, benötige ich entweder daten über den zoom+origin, oder den
        // Sichtbaren y-Intervall. Anders kann ich nicht wissen wie ich die numerischen Werte an den Canvas anpassen soll
        double xStart = drawingInformation.getIntervallX().getItem1();
        double xStop = drawingInformation.getIntervallX().getItem2();
        double xStep = drawingInformation.getStep();
        FunctionInfoContainer[] functionInfo = drawingInformation.getFunctionInfo();
        g2d.setPaint(Color.RED);
        for(FunctionInfoContainer functionContainer: functionInfo){
            IFunction func = functionContainer.getFunction();
            Koordinate[] values = functionContainer.getFunctionValues();
            GeneralPath gp = new GeneralPath();
            gp.moveTo(values[0].getX(),values[1].getY());
            for(int i = 1;i<values.length;i++){
                gp.lineTo(values[i].getX(),values[i].getY());
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
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));
        if(xInterval.getItem1()<=0 && xInterval.getItem2() >=0){
            int x = (int)Utils.mapToInterval(0, xInterval, widthInterval);
            g2d.drawLine(x, 0, x, heightInterval.getItem2());
        }

        if(yInterval.getItem1()<=0 && yInterval.getItem2() >=0){
            int y = (int)Utils.mapToInterval(0, yInterval, heightInterval);
            g2d.drawLine(0,y,widthInterval.getItem2(),y);
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
        int nSquares = (int) (Math.abs(intervallXTuple.getItem2()-intervallXTuple.getItem1())/squareScale);
        System.out.println(nSquares);
        if(nSquares>=25){
            squareScale*=2;
        }else if(nSquares<=10){
            squareScale/=2;
        }
        int xSubStep = (int) Math.round(getWidth()/Math.abs(intervallXTuple.getItem2()-intervallXTuple.getItem1())/subGrid*squareScale);
        int ySubStep = (int) Math.round(getHeight()/Math.abs(intervallYTuple.getItem2()-intervallYTuple.getItem1())/subGrid*squareScale);
        for(double i = (intervallYTuple.getItem1()-intervallYTuple.getItem1()%squareScale-squareScale);i<intervallYTuple.getItem2()+squareScale;i+=squareScale){
            g2d.setStroke(new BasicStroke(2f));
            int y = (int) Utils.mapToInterval(i, intervallYTuple,heightInterval);
            g2d.drawLine(0, y, getWidth(),y );
            g2d.setStroke(new BasicStroke(1f));
            for(int j = 0;j<subGrid;j++){
                g2d.drawLine(0, (int)(y+ySubStep*j), getWidth(),(int)(y+ySubStep*j) );
            }
        }
        for(double i = (intervallXTuple.getItem1()-intervallXTuple.getItem1()%squareScale-squareScale);i<intervallXTuple.getItem2()+squareScale;i+=squareScale){
            g2d.setStroke(new BasicStroke(2f));
            int x = (int) Utils.mapToInterval(i, intervallXTuple,widthInterval);
            g2d.drawLine(x,0,x,getHeight());
            g2d.setStroke(new BasicStroke(1f));
            for(int j = 0;j<subGrid;j++){
                g2d.drawLine((int)(x+xSubStep*j),0,(int)(x+xSubStep*j),getHeight());
            }
        }

        
    }

    /**
     * 
     * @param dPoint Delta-Vektor, um wie viel JPlotter.origin verschoben werden
     *               soll
     */
    public void moveOrigin(Point dPoint) {
        origin.translate(dPoint.x, dPoint.y);
    }

    
    /** 
     * @param factor
     */
    public void moveUp(float factor){
        moveOrigin(new Point(0,(int)factor));
    }
    
    /** 
     * @param factor
     */
    public void moveDown(float factor){
        moveOrigin(new Point(0,(int)-factor));
    }
    
    /** 
     * @param factor
     */
    public void moveRight(float factor){
        moveOrigin(new Point((int)-factor,0));
    }
    
    /** 
     * @param factor
     */
    public void moveLeft(float factor){
        moveOrigin(new Point((int)factor,0));
    }

    /**
     * setter für JPlotter.zoom
     * 
     * @param zoom neuer zoom Wert
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
        repaint();
    }

    /**
     * getter für JPlotter.zoom
     * 
     * @return float
     */
    public double getZoom() {
        return this.zoom;
    }

    public void resetOrigin() {
        origin = new Point(0, 0);
    }

    
    /** Fügt einen PlotListener an das Event an
     * @param plotListener
     */
    public void addPlotListener(IPlotListener plotListener) {
        plotListeners.add(plotListener);
    }
}
