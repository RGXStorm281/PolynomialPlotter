package view.outdated;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.Component;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import view.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.geom.Line2D;
import java.util.function.DoubleFunction;

public class Plotter_Old extends JPanel {

    private float zoom = 1f; // Global zoomlevel 
    // Debug fields
    private boolean zoomed = false;
    private Point debugPoint = new Point(0,0);

    private float spacing = 100; // Hardcoded Space-unit, |spacing| pixels = 1 numerical unit
    private double stepsize = 1.0;
    
    private Point origin; // Point to keep track of the origin point (Used for dragging the screen)
    private Point mousePt; // Point to keep track of the last mouse-position


    public Plotter_Old(){
        // Scroll Listener to handle the Zoom
        addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
               handleMouseWheelMoved(e);
			}
        });
        // Mouse Listener to Handle Drag and change the cursor
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((Plotter_Old) e.getSource()).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((Plotter_Old) e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        // Mouse Motion listener to handle Drag
        addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
                // difference of x,y since the last drag
                float dx = e.getX() - mousePt.x;
                float dy = e.getY() - mousePt.y;
                // Change the origin, based on the differences
                origin.setLocation(origin.x + dx, origin.y + dy);
                System.out.println(origin);
                // Update the original Point to the current position
                mousePt = e.getPoint();
                // Repaint the screen
                repaint();
			}
        });
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F12"),"resetOrigin");
        getActionMap().put("resetOrigin",new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                resetOrigin();
                resetZoom();
                repaint();
            }
        });
        origin = new Point(0,0);
    }

    protected void resetZoom() {
        zoom = 1f;
	}

	protected void handleMouseWheelMoved(MouseWheelEvent e) {
         // Down = 1, Up = -1
         zoomed = true;
         float dZoom=0;
         Plotter_Old self = (Plotter_Old) e.getSource();
         if(e.isControlDown()){
             dZoom = e.getWheelRotation();
             self.zoom-=dZoom;
         }else{
             dZoom = e.getWheelRotation()*0.1f;
             self.zoom-=dZoom;
         }
         // Clamp the zoom to min 0.1f
         if(self.zoom<=0.1)self.zoom=0.1f;
         if(self.zoom>=20)self.zoom=20f;

         // Vector, pointing from the mouse position, towards the origin, scaled by the zoom-delta to create a pseudo effect to zoom towards the mouse.
         float dx = (e.getX()-getWidth()/2-origin.x)*-dZoom;
         float dy = (e.getY()-getHeight()/2-origin.y)*-dZoom;

         debugPoint.setLocation(dx,dy);
        //  origin.setLocation(origin.x-dx*self.zoom,origin.y-dy*self.zoom);
         repaint();
	}

	public void paint(Graphics g){
        int width = this.getWidth();
        int height = this.getHeight();
        Graphics2D g2d = (Graphics2D) g;
        // Enable Antialiasing to get better looking diagonals
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        // Fill the background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,width,height);

        // Handle the transform
        g2d.translate((double) origin.x,(double)origin.y); // Translate towards the origin point (Handles the dragging)
        g2d.translate(width/2, height/2); // Translates to the middle of the screen
        // g2d.scale(zoom,zoom);

        // drawGrid(g2d);
        drawAxes(g2d);
        drawFunction(g2d,x->(float)((x*x)+1),Color.RED);
        drawFunction(g2d,x->(float)((x+1)*(x-1)),Color.BLUE);
        // drawDebug(g2d);
        zoomed = false;
    }

    private void drawDebug(Graphics2D g2d) {
        // Method used for drawing Debug Points or Lines
        // g2d.drawLine(0,0,debugPoint.x, debugPoint.y);
        float x = -1f;
        float y = 10f;
        float unit = spacing;
        float xSpace = getWidth()/unit;
        float xStart = -xSpace/2; 
        float xStop = xSpace/2; 
        float wStart = -getWidth()/2; 
        float wStop = getWidth()/2;
        float[] numericRange = {};
        g2d.drawOval((int)map(x,xStart,xStop,wStart,wStop),(int)-y,10,10);
	}

	private void drawFunction(Graphics2D g2d,DoubleFunction<Float>function,Color color) {
        
        // Set a Constant Stroke width, which scales itself down accordingly to the zoom factor
        g2d.setStroke(new BasicStroke(3));

        double unit = spacing*zoom; // Represents how many Pixels equals 1 (as a numeric value) [Scales up with the zoom factor]
        double xSpace = getWidth()/unit; // How many 1-units are currently on the x-Axis [Maybe refactor this later, to only calculate it when the screen is resized]
        double xStart = -xSpace/2-(origin.x/unit); // Start of the x-Range [Numeric Range-Start of x-Values] Used to only render values that are visible
        double xStop = xSpace/2-(origin.x/unit); // Stop of the x-Range 
        double wStart = -getWidth()/2-origin.x; // Start of the width-Range (used to map the x-numeric-value to a point on the canvas)
        double wStop = getWidth()/2-origin.x;

        double ySpace = getHeight()/unit; // Same as above but with the y-Value
        double yStart = -ySpace/2+(origin.y/unit); // The y-values are different from the x-value because the canvas-coord y values are "upside-down" when mapping normally to a Kartesian Coordinate-System
        double yStop = ySpace/2+(origin.y/unit);
        double hStart = getHeight()/2-origin.y;
        double hStop = -getHeight()/2-origin.y;
        double steps = 0.05f/zoom; // Detail of the Graph [How small is one line on the graph] Scales down with the zoom, to get more detail, as "deeper" we go
        ArrayList<Point.Float> points = new ArrayList<Point.Float>(); // Array List to save the points [Maybe refactor this later, to only push new values in, and pop old values out that are not in vision, instead of creating a new Array on and on]
        for(double i = xStart;i<xStop;i+=steps){
            // Map the x/y-numeric value to the space on the screen
            double x = map(i,xStart,xStop,wStart,wStop); 
            double y = map(function.apply((double)i),yStart,yStop,hStart,hStop);
            points.add(new Point.Float(Math.round(x),Math.round(y)));
        }
        GeneralPath gp = new GeneralPath(); 
        g2d.setPaint(color); 
        gp.moveTo(points.get(0).x, points.get(1).y); // Move the path-start to the first point
        for(int i = 0;i<points.size()-1;i++){
            // Cross a line between every point in the List
            gp.lineTo(points.get(i).x, points.get(i).y);
        }
        // Draw the Path and reset the Stroke Width to 1
        g2d.draw(gp);
        g2d.setStroke(new BasicStroke(1));

	}

	private void drawAxes(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.setFont(GUI.getFont(25));

        g2d.drawLine((int)-((getWidth()/2+origin.x)), (int)(0), (int)((getWidth()/2-origin.x)),(int)(0)); // X-Axis Main Line
        drawXSteps(g2d); // Draw the Steps on the X-Axis

        g2d.drawLine((int)0, (int)-((getHeight()/2+origin.y)), (int)(0),(int)((getHeight()/2-origin.y))); // Y-Axis Main Line
        drawYSteps(g2d); // Draw the Steps on the Y-Axis
        g2d.setStroke(new BasicStroke(1));
	}

    private void drawXSteps(Graphics2D g2d){
        double unit = spacing*zoom;
        double xSpace = getWidth()/unit; // How many 1-units are currently on the x-Axis [Maybe refactor this later, to only calculate it when the screen is resized]
        double xStart = -xSpace/2-(origin.x/unit); // Start of the x-Range [Numeric Range-Start of x-Values] Used to only render values that are visible
        double xStop = xSpace/2-(origin.x/unit); // Stop of the x-Range 
        double wStart = -getWidth()/2-origin.x; // Start of the width-Range (used to map the x-numeric-value to a point on the canvas)
        double wStop = getWidth()/2-origin.x;
        if(xSpace/stepsize > 20){
            stepsize*=10;
        }
        if(xSpace/stepsize<5){
            stepsize/=10;
        }
        System.out.println(zoom);
        DecimalFormat df = new DecimalFormat(stepsize-stepsize+"");
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        double textHeight = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "-1").getVisualBounds().getHeight();
        for(float i = (float)(xStart-xStart%(stepsize));i<xStop;i+=stepsize){
            if(i < stepsize-stepsize/10 && i > -stepsize+stepsize/10)continue;
            double x = map((double)i,xStart,xStop,wStart,wStop);
            g2d.draw(new Line2D.Double(x,10d,x,-10d));
            float textWidth = g2d.getFontMetrics().stringWidth(df.format(i)); // Text-Width to center the text over the marking
            g2d.drawString(df.format(i),(int)x-textWidth/2,(int)-textHeight-15);
            }
    }
    
    private void drawYSteps(Graphics2D g2d){
        double unit = spacing*zoom;
        double ySpace = getHeight()/unit; // How many 1-units are currently on the x-Axis [Maybe refactor this later, to only calculate it when the screen is resized]
        double yStart = -ySpace/2-(origin.y/unit); // Start of the x-Range [Numeric Range-Start of x-Values] Used to only render values that are visible
        double yStop = ySpace/2-(origin.y/unit); // Stop of the x-Range 
        double hStart = -getHeight()/2-origin.y; // Start of the width-Range (used to map the x-numeric-value to a point on the canvas)
        double hStop = getHeight()/2-origin.y;
        System.out.println(zoom);
        DecimalFormat df = new DecimalFormat(stepsize-stepsize+"");
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        double textHeight = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "-1").getVisualBounds().getHeight();
        for(float i = (float)(yStart-yStart%(stepsize));i<yStop;i+=stepsize){
            if(i < stepsize-stepsize/10 && i > -stepsize+stepsize/10)continue;
            double y = map((double)i,yStart,yStop,hStart,hStop);
            float textWidth = g2d.getFontMetrics().stringWidth(df.format(-i)); // Text-Width to center the text over the marking
            g2d.draw(new Line2D.Double(10d,y,-10d,y));
            g2d.drawString(df.format(-i),-textWidth-15,(int)(y+textHeight/2));
            }
    }
	public void drawGrid(Graphics2D g2d){
        g2d.setStroke(new BasicStroke((1/zoom)*4));
        g2d.setColor(Color.BLUE);
        int width = this.getWidth();
        int height = this.getHeight();
        g2d.drawLine((int)-spacing,(int)(-height/2-origin.y),(int)-spacing,(int)(height/2-origin.y));
        // float xCorrection = (origin.x%(spacing*zoom));
        // float yCorrection = (origin.y%(spacing*zoom));
        // g2d.setColor(new Color(200,200,200));
        // for(float x = (-width/2-origin.x+xCorrection)/zoom;x<(width/2-origin.x+xCorrection)/zoom;x+=spacing){ // Y-Axis-Girds
        //     g2d.drawLine((int)x, (int)-((getHeight()/2+origin.y)/zoom), (int)(x),(int)((getHeight()/2-origin.y)/zoom));
        // }
        // for(float y = (-height/2-origin.y+yCorrection)/zoom;y<(height/2-origin.y+yCorrection)/zoom;y+=spacing){
        //     g2d.drawLine((int)-((getWidth()/2+origin.x)/zoom), (int)(y), (int)((getWidth()/2-origin.x)/zoom),(int)(y));
        // }
    }

    public void setZoom(float zoom){
        this.zoom = zoom;
        this.repaint();
    }

    public float getZoom(){
        return this.zoom;
    }

    static public final float map(float value, float start1, float stop1, float start2, float stop2) {
		float outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
		return outgoing;
	}

    static public final double map(double value, double start1, double stop1, double start2, double stop2) {
		double outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
		return outgoing;
	}

    public void resetOrigin() {
        origin = new Point(0,0);
    }
}
