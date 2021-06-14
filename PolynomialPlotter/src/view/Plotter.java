package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.geom.Line2D;
import java.util.function.DoubleFunction;

public class Plotter extends JPanel {

    private float zoom = 1f; // Global zoomlevel 
    // Debug fields
    private boolean zoomed = false;
    private Point debugPoint = new Point(0,0);

    private float spacing = 100; // Hardcoded Space-unit, |spacing| pixels = 1 numerical unit
    private Point origin; // Point to keep track of the origin point (Used for dragging the screen)
    private Point mousePt; // Point to keep track of the last mouse-position


    public Plotter(){
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
                ((Plotter) e.getSource()).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((Plotter) e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
        origin = new Point(0,0);
    }

    protected void handleMouseWheelMoved(MouseWheelEvent e) {
         // Down = 1, Up = -1
         zoomed = true;
         float dZoom=0;
         Plotter self = (Plotter) e.getSource();
         if(e.isControlDown()){
             dZoom = e.getWheelRotation();
             self.zoom-=dZoom;
         }else{
             dZoom = e.getWheelRotation()*0.1f;
             self.zoom-=dZoom;
         }
         // Clamp the zoom to min 0.1f
         if(self.zoom<=0.1)self.zoom=0.1f;

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
        g2d.scale(zoom,zoom);

        // drawGrid(g2d);
        drawAxes(g2d);
        drawFunction(g2d,x->(float)(x*x),Color.RED);
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
        g2d.setStroke(new BasicStroke((1/zoom)*3));

        double unit = spacing; // Represents how many Pixels equals 1 (as a numeric value)
        double xSpace = getWidth()/unit; // How many units are on the x-Axis
        double xStart = -xSpace/2; // Start of the x-Range
        double xStop = xSpace/2; // Stop of the x-Range
        double wStart = -getWidth()/2; // Start of the width-Range (used to map the x-value to a point on the canvas)
        double wStop = getWidth()/2;

        double ySpace = getHeight()/unit;
        double yStart = -ySpace/2;
        double yStop = ySpace/2;
        double hStart = getHeight()/2;
        double hStop = -getHeight()/2;
        double steps = 0.005f; // Detail of the Graph
        ArrayList<Point.Float> points = new ArrayList<Point.Float>();
        for(double i = xStart;i<xStop;i+=steps){
            // Map the x/y-numeric value to the space on the screen
            double x = map(i,xStart,xStop,wStart,wStop); 
            double y = map(function.apply((double)i),yStart,yStop,hStart,hStop);
            points.add(new Point.Float(Math.round(x),Math.round(y)));
        }
        GeneralPath gp = new GeneralPath();
        g2d.setPaint(color);
        // Move to the first point
        gp.moveTo(points.get(0).x, points.get(1).y);
        BufferedImage img = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        for(int i = 0;i<points.size()-1;i++){
            // Draw a line between every point in the List
            g2d.draw(new Line2D.Float(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y));
            // gp.lineTo(points.get(i).x, points.get(i).y);
            // img.setRGB(points.get(i).x,points.get(i).y,color.getRGB());
        }


        // Draw the Path and reset the Stroke Width
        g2d.draw(gp);
        // g2d.drawImage(img,-getWidth()/2-origin.x,-getHeight()-origin.y,getWidth()/2-origin.x,getHeight()/2-origin.y,null);
        g2d.setStroke(new BasicStroke(1));

	}

	private void drawAxes(Graphics2D g2d) {
        // Set Constant-Stroke width which scales itself down with the zoom-factor
        g2d.setStroke(new BasicStroke((1/zoom)*2));
        g2d.setColor(Color.BLACK);
        g2d.setFont(GUI.getFont((1/zoom)*25));
        // Draw X-Axis
        g2d.drawLine((int)-((getWidth()/2+origin.x)/zoom), (int)(0), (int)((getWidth()/2-origin.x)/zoom),(int)(0)); // Main Lines
        g2d.drawLine((int)-spacing,(int)((1/zoom)*10),(int)-spacing,(int)-((1/zoom)*10));
        g2d.drawString("-1",-spacing,(1/zoom)*-10);
        g2d.drawLine((int)0, (int)-((getHeight()/2+origin.y)/zoom), (int)(0),(int)((getHeight()/2-origin.y)/zoom)); // draw  Y-Axis
        g2d.setStroke(new BasicStroke(1));
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
}
