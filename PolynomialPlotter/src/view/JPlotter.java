package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.DrawingInformationContainer;
import startup.Settings;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.geom.Line2D;
import java.util.function.DoubleFunction;

public class JPlotter extends JPanel {

    private double zoom; // Global zoomlevel

    private final float SPACING; // Hardcoded Space-unit, |spacing| pixels = 1 numerical unit


    
    private Point origin; // Point to keep track of the origin point (Used for dragging the screen)
    private Point mousePt; // Point to keep track of the last mouse-position
    private Settings settings;
    // Temporäre Vars
    private double stepsize = 1.0; // In welchen Abständen werden markierungen auf x/y-Achse angezeigt

    private DrawingInformationContainer drawingInformation;

    public JPlotter(Settings settings) {
        // Scroll-Listener --> Zoom
        this.settings = settings;
        this.zoom = settings.INITIAL_ZOOM;
        this.SPACING = settings.INITIAL_PIXEL_TO_UNIT_RATIO;
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                handleMouseWheelZoom(e);
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
        });
        // MouseMotionListener für die Drag-Funktionalität
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Differenz zwischen der momentanten und letzten Maus-Position
                float dx = e.getX() - mousePt.x;
                float dy = e.getY() - mousePt.y;
                // Ändere den Ursprung, basierend auf dx,dy
                origin.setLocation(origin.x + dx, origin.y + dy);
                mousePt = e.getPoint();
                repaint();
            }
        });
        // Hotkeys - Wird noch in eine Extra Klasse gepackt
        InputMap inputmap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionmap = getActionMap();
        inputmap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputmap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputmap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputmap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        inputmap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        inputmap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputmap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputmap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,0), "zoomIn");
        inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,0), "zoomOut");
        actionmap.put("moveUp", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                moveUp(10);
                repaint();
            }
        });
        actionmap.put("moveDown", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                moveDown(10);
                repaint();
            }
        });
        actionmap.put("moveLeft", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                moveLeft(10);
                repaint();
            }
        });
        actionmap.put("moveRight", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                moveRight(10);
                repaint();
            }
        });
        inputmap.put(KeyStroke.getKeyStroke("F12"), "resetOrigin");
        actionmap.put("resetOrigin", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                resetOrigin();
                resetZoom();
                repaint();
            }
        });
        actionmap.put("zoomIn", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                zoomIn(0.01f);
                repaint();
            }
        });
        actionmap.put("zoomOut", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                zoomOut(0.01f);
                repaint();
            }
        });
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
    protected void handleMouseWheelZoom(MouseWheelEvent e) {
        float factor = e.isControlDown() ? 1f : 0.1f; // Wenn man STRG gedrückt hält, zoomt man schneller rein und raus
        switch (e.getWheelRotation()) {
            case 1:
                zoomOut(factor);
                break;
            case -1:
                zoomIn(factor);
                break;
        }
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

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Antialiasing für bessere Diagonalen
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Hintergrund-Füllung
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.translate((double) origin.x, (double) origin.y); // View abhängig vom Ursprung translaten, wichtig für die
                                                             // Drag-Funktionalität
        g2d.translate(getWidth() / 2, getHeight() / 2); // View auf die Mitte des Panels translaten

        drawAxes(g2d);
        drawFunction(g2d, x -> (float) ((x * x) + 1), Color.RED);
        drawFunction(g2d, x -> (float) (Math.sin(x) * x * x), Color.BLUE);
    }

    /**
     * Funktion um Debug-Punkte etc. zu malen, wird final gelöscht
     * 
     * @param g2d Graphics2D context
     */
    private void drawDebug(Graphics2D g2d) {

        float x = -1f;
        float y = 10f;
        float unit = SPACING;
        float xSpace = getWidth() / unit;
        float xStart = -xSpace / 2;
        float xStop = xSpace / 2;
        float wStart = -getWidth() / 2;
        float wStop = getWidth() / 2;
        float[] numericRange = {};
        g2d.drawOval((int) map(x, xStart, xStop, wStart, wStop), (int) -y, 10, 10);
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
    private void drawFunction(Graphics2D g2d, DoubleFunction<Float> function, Color color) {

        // Set a Constant Stroke width, which scales itself down accordingly to the zoom
        // factor
        g2d.setStroke(new BasicStroke(3));

        double unit = SPACING * zoom; // Represents how many Pixels equals 1 (as a numeric value) [Scales up with the
                                      // zoom factor]
        double xSpace = getWidth() / unit; // How many 1-units are currently on the x-Axis [Maybe refactor this later,
                                           // to only calculate it when the screen is resized]
        double xStart = -xSpace / 2 - (origin.x / unit); // Start of the x-Range [Numeric Range-Start of x-Values] Used
                                                         // to only render values that are visible
        double xStop = xSpace / 2 - (origin.x / unit); // Stop of the x-Range
        double wStart = -getWidth() / 2 - origin.x; // Start of the width-Range (used to map the x-numeric-value to a
                                                    // point on the canvas)
        double wStop = getWidth() / 2 - origin.x;

        double ySpace = getHeight() / unit; // Same as above but with the y-Value
        double yStart = -ySpace / 2 + (origin.y / unit); // The y-values are different from the x-value because the
                                                         // canvas-coord y values are "upside-down" when mapping
                                                         // normally to a Kartesian Coordinate-System
        double yStop = ySpace / 2 + (origin.y / unit);
        double hStart = getHeight() / 2 - origin.y;
        double hStop = -getHeight() / 2 - origin.y;
        double steps = 0.005f / zoom; // Detail of the Graph [How small is one line on the graph] Scales down with the
                                      // zoom, to get more detail, as "deeper" we go
        ArrayList<Point.Float> points = new ArrayList<Point.Float>(); // Array List to save the points [Maybe refactor
                                                                      // this later, to only push new values in, and pop
                                                                      // old values out that are not in vision, instead
                                                                      // of creating a new Array on and on]
        for (double i = xStart; i < xStop; i += steps) {
            // Map the x/y-numeric value to the space on the screen
            double x = map(i, xStart, xStop, wStart, wStop);
            double y = map(function.apply((double) i), yStart, yStop, hStart, hStop);
            points.add(new Point.Float(Math.round(x), Math.round(y)));
        }
        GeneralPath gp = new GeneralPath();
        g2d.setPaint(color);
        gp.moveTo(points.get(0).x, points.get(1).y); // Move the path-start to the first point
        for (int i = 0; i < points.size() - 1; i++) {
            // Cross a line between every point in the List
            gp.lineTo(points.get(i).x, points.get(i).y);
        }
        // Draw the Path and reset the Stroke Width to 1
        g2d.draw(gp);
        g2d.setStroke(new BasicStroke(1));

    }

    /**
     * Zeichnet die X und Y Achsen auf den Bildschirm (Abhängig von JPlotter.origin)
     * 
     * @param g2d Graphics2D context
     */
    private void drawAxes(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.setFont(GUI.getFont(25));

        g2d.drawLine((int) -((getWidth() / 2 + origin.x)), (int) (0), (int) ((getWidth() / 2 - origin.x)), (int) (0)); // X-Axis
                                                                                                                       // Main
                                                                                                                       // Line
        drawXSteps(g2d); // Draw the Steps on the X-Axis

        g2d.drawLine((int) 0, (int) -((getHeight() / 2 + origin.y)), (int) (0), (int) ((getHeight() / 2 - origin.y))); // Y-Axis
                                                                                                                       // Main
                                                                                                                       // Line
        drawYSteps(g2d); // Draw the Steps on the Y-Axis
        g2d.setStroke(new BasicStroke(1));
    }

    /**
     * Zeichnet die X-Achsen Schritte mit Benenneung
     * 
     * @param g2d Graphics2D context
     */
    private void drawXSteps(Graphics2D g2d) {
        double unit = SPACING * zoom;
        // TODO: xSpace und ySpace Als Globales Attribut deklarieren und nur ändern wenn
        // man den Screen resized
        double xSpace = getWidth() / unit; // Wie viele 1er Werte (numerisch) passen auf die x-Achse
        double xStart = -xSpace / 2 - (origin.x / unit); // Start der x-Werte [Numerisch], damit nur sichtbare werte
                                                         // gerechnet werden
        double xStop = xSpace / 2 - (origin.x / unit); // Gegenstück zum Start
        double wStart = -getWidth() / 2 - origin.x; // Start der Breiten-Werten [Später wird die x-Menge auf die
                                                    // Breiten-Menge gemappt]
        double wStop = getWidth() / 2 - origin.x;
        // Pseudo dynamische anpassung der stepsize Abhängig vom verfügbaren Platz die
        // stepsize entweder größer, oder kleiner ziehen
        if (xSpace / stepsize > 20) {
            stepsize *= 10;
        }
        if (xSpace / stepsize < 5) {
            stepsize /= 10;
        }
        DecimalFormat df = new DecimalFormat(stepsize - stepsize + ""); // Decimal-Format um die Zahlen Später auf eine
                                                                        // lesbare länge zu bringen
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        double textHeight = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "-1").getVisualBounds()
                .getHeight();
        for (float i = (float) (xStart - xStart % (stepsize)); i < xStop; i += stepsize) { // Für jede Markierung,
                                                                                           // startend an der Linken,
                                                                                           // bis zur rechten
            if (i < stepsize - stepsize / 10 && i > -stepsize + stepsize / 10)
                continue; // Wenn (-stepsize+correction) < i < (stepsize-correction), dann ist i ~ 0 ->
                          // Skip
            double x = map((double) i, xStart, xStop, wStart, wStop); // Numerischen Wert auf die Breiten-Range mappen
            g2d.draw(new Line2D.Double(x, 10d, x, -10d));
            float textWidth = g2d.getFontMetrics().stringWidth(df.format(i));
            g2d.drawString(df.format(i), (int) x - textWidth / 2, (int) -textHeight - 15);
        }
    }

    /**
     * Zeichnet die Y-Achsen Schritte mit Benennung
     * 
     * @param g2d Graphics2D context
     */
    private void drawYSteps(Graphics2D g2d) {
        double unit = SPACING * zoom;
        double ySpace = getHeight() / unit;
        double yStart = -ySpace / 2 - (origin.y / unit);
        double yStop = ySpace / 2 - (origin.y / unit);
        double hStart = -getHeight() / 2 - origin.y;
        double hStop = getHeight() / 2 - origin.y;
        DecimalFormat df = new DecimalFormat(stepsize - stepsize + "");
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        double textHeight = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "-1").getVisualBounds()
                .getHeight();
        for (float i = (float) (yStart - yStart % (stepsize)); i < yStop; i += stepsize) {
            if (i < stepsize - stepsize / 10 && i > -stepsize + stepsize / 10)
                continue;
            double y = map((double) i, yStart, yStop, hStart, hStop);
            float textWidth = g2d.getFontMetrics().stringWidth(df.format(-i));
            g2d.draw(new Line2D.Double(10d, y, -10d, y));
            g2d.drawString(df.format(-i), -textWidth - 15, (int) (y + textHeight / 2));
        }
    }

    /**
     * Zeichnet ein Karierten Hintergrund, basierend auf JPlotter.origin und
     * JPlotter.zoom
     * 
     * @param g2d Graphics2D context
     */
    public void drawGrid(Graphics2D g2d) {
        // Not ready yet
    }

    /**
     * 
     * @param dPoint Delta-Vektor, um wie viel JPlotter.origin verschoben werden
     *               soll
     */
    public void moveOrigin(Point dPoint) {
        origin.translate(dPoint.x, dPoint.y);
    }

    public void moveUp(float factor){
        moveOrigin(new Point(0,(int)factor));
    }
    public void moveDown(float factor){
        moveOrigin(new Point(0,(int)-factor));
    }
    public void moveRight(float factor){
        moveOrigin(new Point((int)-factor,0));
    }
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

    /**
     * Remappt eine Zahl von einer vorgegebenen Range auf eine andere vorgegebene
     * Range
     * 
     * @param value  zu mappender Wert (aus Range 1)
     * @param start1 Start-Wert Range 1
     * @param stop1  Stop-Wert Range 1
     * @param start2 Start-Wert Range 2
     * @param stop2  Stop-Wert Range 2
     * @return float value auf Range 2 gemappt
     */
    static public final float map(float value, float start1, float stop1, float start2, float stop2) {
        float outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }

    /**
     * Remappt eine Zahl von einer vorgegebenen Range auf eine andere vorgegebene
     * Range
     * 
     * @param value  zu mappender Wert (aus Range 1)
     * @param start1 Start-Wert Range 1
     * @param stop1  Stop-Wert Range 1
     * @param start2 Start-Wert Range 2
     * @param stop2  Stop-Wert Range 2
     * @return double value auf Range 2 gemappt
     */
    static public final double map(double value, double start1, double stop1, double start2, double stop2) {
        double outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }

    public void resetOrigin() {
        origin = new Point(0, 0);
    }
}
