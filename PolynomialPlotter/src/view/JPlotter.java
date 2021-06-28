package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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

public class JPlotter extends JPanel {

    private double zoom; // Global zoomlevel

    private final float SPACING; // Hardcoded Space-unit, |spacing| pixels = 1 numerical unit


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
        // // Hotkeys - Wird noch in eine Extra Klasse gepackt
        // InputMap inputmap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        // ActionMap actionmap = getActionMap();
        // inputmap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        // inputmap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        // inputmap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        // inputmap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        // inputmap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        // inputmap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        // inputmap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        // inputmap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        // inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,0), "zoomIn");
        // inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,0), "zoomOut");
        // actionmap.put("moveUp", new AbstractAction(){
        //     public void actionPerformed(ActionEvent e) {
        //         moveUp(10);
        //         repaint();
        //     }
        // });
        // actionmap.put("moveDown", new AbstractAction(){
        //     public void actionPerformed(ActionEvent e) {
        //         moveDown(10);
        //         repaint();
        //     }
        // });
        // actionmap.put("moveLeft", new AbstractAction(){
        //     public void actionPerformed(ActionEvent e) {
        //         moveLeft(10);
        //         repaint();
        //     }
        // });
        // actionmap.put("moveRight", new AbstractAction(){
        //     public void actionPerformed(ActionEvent e) {
        //         moveRight(10);
        //         repaint();
        //     }
        // });
        // inputmap.put(KeyStroke.getKeyStroke("F12"), "resetOrigin");
        // actionmap.put("resetOrigin", new AbstractAction() {
        //     public void actionPerformed(ActionEvent e) {
        //         resetOrigin();
        //         resetZoom();
        //         repaint();
        //     }
        // });
        // actionmap.put("zoomIn", new AbstractAction() {
        //     public void actionPerformed(ActionEvent e) {
        //         zoomIn(0.01f);
        //         repaint();
        //     }
        // });
        // actionmap.put("zoomOut", new AbstractAction() {
        //     public void actionPerformed(ActionEvent e) {
        //         zoomOut(0.01f);
        //         repaint();
        //     }
        // });
        // Setzte den Ursprung initial auf 0,0
        setPreferredSize(new Dimension(settings.INITIAL_PLOT_WIDTH,settings.INITIAL_PLOT_HEIGHT));
        origin = new Point(settings.INITIAL_ORIGIN_X, settings.INITIAL_ORIGIN_Y);
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
        drawFunctions(g2d);
        // drawAxes(g2d);
        drawGrid(g2d);
        // drawFunction(g2d, x -> (float) ((x * x) + 1), Color.RED);
        // drawFunction(g2d, x -> (float) (Math.sin(x) * x * x), Color.BLUE);
    }

    
    /** 
     * @param g2d
     */
    private void drawFunctions(Graphics2D g2d) {
        if(drawingInformation == null)return;
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
    // private void drawFunction(Graphics2D g2d, DoubleFunction<Float> function, Color color) {

    //     // Set a Constant Stroke width, which scales itself down accordingly to the zoom
    //     // factor
    //     g2d.setStroke(new BasicStroke(3));

    //     double unit = SPACING * zoom; // Represents how many Pixels equals 1 (as a numeric value) [Scales up with the
    //                                   // zoom factor]
    //     double xSpace = getWidth() / unit; // How many 1-units are currently on the x-Axis [Maybe refactor this later,
    //                                        // to only calculate it when the screen is resized]
    //     double xStart = -xSpace / 2 - (origin.x / unit); // Start of the x-Range [Numeric Range-Start of x-Values] Used
    //                                                      // to only render values that are visible
    //     double xStop = xSpace / 2 - (origin.x / unit); // Stop of the x-Range
    //     double wStart = -getWidth() / 2 - origin.x; // Start of the width-Range (used to map the x-numeric-value to a
    //                                                 // point on the canvas)
    //     double wStop = getWidth() / 2 - origin.x;

    //     double ySpace = getHeight() / unit; // Same as above but with the y-Value
    //     double yStart = -ySpace / 2 + (origin.y / unit); // The y-values are different from the x-value because the
    //                                                      // canvas-coord y values are "upside-down" when mapping
    //                                                      // normally to a Kartesian Coordinate-System
    //     double yStop = ySpace / 2 + (origin.y / unit);
    //     double hStart = getHeight() / 2 - origin.y;
    //     double hStop = -getHeight() / 2 - origin.y;
    //     double steps = 0.005f / zoom; // Detail of the Graph [How small is one line on the graph] Scales down with the
    //                                   // zoom, to get more detail, as "deeper" we go
    //     ArrayList<Point.Float> points = new ArrayList<Point.Float>(); // Array List to save the points [Maybe refactor
    //                                                                   // this later, to only push new values in, and pop
    //                                                                   // old values out that are not in vision, instead
    //                                                                   // of creating a new Array on and on]
    //     for (double i = xStart; i < xStop; i += steps) {
    //         // Map the x/y-numeric value to the space on the screen
    //         double x = map(i, xStart, xStop, wStart, wStop);
    //         double y = map(function.apply((double) i), yStart, yStop, hStart, hStop);
    //         points.add(new Point.Float(Math.round(x), Math.round(y)));
    //     }
    //     GeneralPath gp = new GeneralPath();
    //     g2d.setPaint(color);
    //     gp.moveTo(points.get(0).x, points.get(1).y); // Move the path-start to the first point
    //     for (int i = 0; i < points.size() - 1; i++) {
    //         // Cross a line between every point in the List
    //         gp.lineTo(points.get(i).x, points.get(i).y);
    //     }
    //     // Draw the Path and reset the Stroke Width to 1
    //     g2d.draw(gp);
    //     g2d.setStroke(new BasicStroke(1));

    // }

    /**
     * Zeichnet die X und Y Achsen auf den Bildschirm (Abhängig von JPlotter.origin)
     * 
     * @param g2d Graphics2D context
     */
    private void drawAxes(Graphics2D g2d) {
        drawingInformation.getIntervallX();
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.setFont(GUI.getFont(FontFamily.RUBIK,FontStyle.REGULAR,25));

        g2d.drawLine((int) -((getWidth() / 2 + origin.x)), (int) (0), (int) ((getWidth() / 2 - origin.x)), (int) (0)); // X-Axis
                                                                                                                       // Main
                                                                                                                       // Line
        // drawXSteps(g2d); // Draw the Steps on the X-Axis

        g2d.drawLine((int) 0, (int) -((getHeight() / 2 + origin.y)), (int) (0), (int) ((getHeight() / 2 - origin.y))); // Y-Axis
                                                                                                                       // Main
                                                                                                                       // Line
        // drawYSteps(g2d); // Draw the Steps on the Y-Axis
        g2d.setStroke(new BasicStroke(1));
    }

    /**
     * Zeichnet die X-Achsen Schritte mit Benenneung
     * 
     * @param g2d Graphics2D context
     */
    // private void drawXSteps(Graphics2D g2d) {
    //     double unit = SPACING * zoom;
    //     // TODO: xSpace und ySpace Als Globales Attribut deklarieren und nur ändern wenn
    //     // man den Screen resized
    //     double xSpace = getWidth() / unit; // Wie viele 1er Werte (numerisch) passen auf die x-Achse
    //     double xStart = -xSpace / 2 - (origin.x / unit); // Start der x-Werte [Numerisch], damit nur sichtbare werte
    //                                                      // gerechnet werden
    //     double xStop = xSpace / 2 - (origin.x / unit); // Gegenstück zum Start
    //     double wStart = -getWidth() / 2 - origin.x; // Start der Breiten-Werten [Später wird die x-Menge auf die
    //                                                 // Breiten-Menge gemappt]
    //     double wStop = getWidth() / 2 - origin.x;
    //     // Pseudo dynamische anpassung der stepsize Abhängig vom verfügbaren Platz die
    //     // stepsize entweder größer, oder kleiner ziehen
    //     if (xSpace / stepsize > 20) {
    //         stepsize *= 10;
    //     }
    //     if (xSpace / stepsize < 5) {
    //         stepsize /= 10;
    //     }
    //     DecimalFormat df = new DecimalFormat(stepsize - stepsize + ""); // Decimal-Format um die Zahlen Später auf eine
    //                                                                     // lesbare länge zu bringen
    //     df.setRoundingMode(RoundingMode.HALF_DOWN);
    //     double textHeight = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "-1").getVisualBounds()
    //             .getHeight();
    //     for (float i = (float) (xStart - xStart % (stepsize)); i < xStop; i += stepsize) { // Für jede Markierung,
    //                                                                                        // startend an der Linken,
    //                                                                                        // bis zur rechten
    //         if (i < stepsize - stepsize / 10 && i > -stepsize + stepsize / 10)
    //             continue; // Wenn (-stepsize+correction) < i < (stepsize-correction), dann ist i ~ 0 ->
    //                       // Skip
    //         double x = map((double) i, xStart, xStop, wStart, wStop); // Numerischen Wert auf die Breiten-Range mappen
    //         g2d.draw(new Line2D.Double(x, 10d, x, -10d));
    //         float textWidth = g2d.getFontMetrics().stringWidth(df.format(i));
    //         g2d.drawString(df.format(i), (int) x - textWidth / 2, (int) -textHeight - 15);
    //     }
    // }

    // /**
    //  * Zeichnet die Y-Achsen Schritte mit Benennung
    //  * 
    //  * @param g2d Graphics2D context
    //  */
    // private void drawYSteps(Graphics2D g2d) {
    //     double unit = SPACING * zoom;
    //     double ySpace = getHeight() / unit;
    //     double yStart = -ySpace / 2 - (origin.y / unit);
    //     double yStop = ySpace / 2 - (origin.y / unit);
    //     double hStart = -getHeight() / 2 - origin.y;
    //     double hStop = getHeight() / 2 - origin.y;
    //     DecimalFormat df = new DecimalFormat(stepsize - stepsize + "");
    //     df.setRoundingMode(RoundingMode.HALF_DOWN);
    //     double textHeight = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "-1").getVisualBounds()
    //             .getHeight();
    //     for (float i = (float) (yStart - yStart % (stepsize)); i < yStop; i += stepsize) {
    //         if (i < stepsize - stepsize / 10 && i > -stepsize + stepsize / 10)
    //             continue;
    //         double y = map((double) i, yStart, yStop, hStart, hStop);
    //         float textWidth = g2d.getFontMetrics().stringWidth(df.format(-i));
    //         g2d.draw(new Line2D.Double(10d, y, -10d, y));
    //         g2d.drawString(df.format(-i), -textWidth - 15, (int) (y + textHeight / 2));
    //     }
    // }

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
        Tuple<Integer,Integer> heightIntervall = new Tuple<Integer,Integer>(0, getHeight());
        Tuple<Integer,Integer> widthIntervall = new Tuple<Integer,Integer>(0, getWidth());
        int xSubStep = (int) Math.round(getWidth()/Math.abs(intervallXTuple.getItem2()-intervallXTuple.getItem1())/subGrid);
        int ySubStep = (int) Math.round(getHeight()/Math.abs(intervallYTuple.getItem2()-intervallYTuple.getItem1())/subGrid);
        for(int i = (int)(intervallYTuple.getItem1()-intervallYTuple.getItem1()%1)-1;i<intervallYTuple.getItem2()+1;i++){
            g2d.setStroke(new BasicStroke(2f));
            int y = (int) Utils.mapToInterval(i, intervallYTuple,heightIntervall);
            g2d.drawLine(0, y, getWidth(),y );
            g2d.setStroke(new BasicStroke(1f));
            for(int j = 0;j<subGrid;j++){
                g2d.drawLine(0, (int)(y+ySubStep*j), getWidth(),(int)(y+ySubStep*j) );
            }
        }
        for(int i = (int)(intervallXTuple.getItem1()-intervallXTuple.getItem1()%1)-1;i<intervallXTuple.getItem2()+1;i++){
            g2d.setStroke(new BasicStroke(2f));
            int x = (int) Utils.mapToInterval(i, intervallXTuple,widthIntervall);
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
