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
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.function.DoubleFunction;

public class Plotter extends JPanel {

    private float zoom = 1f;
    private boolean zoomed = false;
    private Point debugPoint = new Point(0,0);
    private float spacing = 100;
    private Point origin;
    private Point mousePt;


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
                Plotter self = (Plotter) e.getSource();
                self.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Plotter self = (Plotter) e.getSource();
                self.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
         // Create a Vektor with

         float dx = (e.getX()-getWidth()/2-origin.x)*-dZoom;
         float dy = (e.getY()-getHeight()/2-origin.y)*-dZoom;
        //  float len = (float) Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        //  dx = dx/len*(-dZoom*len);
        //  dy = dy/len*(-dZoom*len);
         debugPoint.setLocation(dx,dy);
         origin.setLocation(origin.x-dx*self.zoom,origin.y-dy*self.zoom);
         repaint();
	}

	public void paint(Graphics g){
        int width = this.getWidth();
        int height = this.getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,width,height);
        g2d.translate((double) origin.x,(double)origin.y);
        g2d.translate(width/2, height/2);
        g2d.scale(zoom,zoom);
        // drawGrid(g2d);
        drawAxes(g2d);
        drawFunction(g2d,x->(float)(x*x),Color.RED);
        drawFunction(g2d,x->(float)(x*x*x),Color.BLUE);
        drawDebug(g2d);
        zoomed = false;
    }

    private void drawDebug(Graphics2D g2d) {
        g2d.drawLine(0,0,debugPoint.x, debugPoint.y);
	}

	private void drawFunction(Graphics2D g2d,DoubleFunction<Float>function,Color color) {
        g2d.setStroke(new BasicStroke((1/zoom)*3));
        float unit = spacing;
        float xSpace = getWidth()/unit;
        float xStart = -xSpace/2;
        float xStop = xSpace/2;
        float wStart = -getWidth()/2*zoom;
        float wStop = getWidth()/2*zoom;
        float ySpace = getHeight()/unit;
        float yStart = -ySpace/2;
        float yStop = ySpace/2;
        float hStart = getHeight()/2*zoom;
        float hStop = -getHeight()/2*zoom;
        float steps = 0.005f;
        ArrayList<Point> points = new ArrayList<Point>();
        for(float i = xStart;i<xStop;i+=steps){
            float x = map(i,xStart,xStop,wStart,wStop);
            float y = map(function.apply((double)i),yStart,yStop,hStart,hStop);
            points.add(new Point((int)x,(int)y));
        }
        GeneralPath gp = new GeneralPath();
        g2d.setPaint(color);
        gp.moveTo(points.get(0).x, points.get(1).y);
        for(int i = 1;i<points.size();i++){
            gp.lineTo(points.get(i).x, points.get(i).y);
        }
        g2d.draw(gp);
        g2d.setStroke(new BasicStroke(1));

	}

	private void drawAxes(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke((1/zoom)*2));
        g2d.setColor(Color.BLACK);
        g2d.drawLine((int)-((getWidth()/2+origin.x)/zoom), (int)(0), (int)((getWidth()/2-origin.x)/zoom),(int)(0)); // X-Axis
        g2d.drawLine((int)0, (int)-((getHeight()/2+origin.y)/zoom), (int)(0),(int)((getHeight()/2-origin.y)/zoom)); // Y-Axis
        g2d.setStroke(new BasicStroke(1));
	}

	private void drawOrientation(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawOval(0,0, 100,100);
	}

	public void drawGrid(Graphics2D g2d){
        int width = this.getWidth();
        int height = this.getHeight();
        float xCorrection = (origin.x%(spacing*zoom));
        float yCorrection = (origin.y%(spacing*zoom));
        g2d.setColor(new Color(200,200,200));
        for(float x = (-width/2-origin.x+xCorrection)/zoom;x<(width/2-origin.x+xCorrection)/zoom;x+=spacing){ // Y-Axis-Girds
            g2d.drawLine((int)x, (int)-((getHeight()/2+origin.y)/zoom), (int)(x),(int)((getHeight()/2-origin.y)/zoom));
        }
        for(float y = (-height/2-origin.y+yCorrection)/zoom;y<(height/2-origin.y+yCorrection)/zoom;y+=spacing){
            g2d.drawLine((int)-((getWidth()/2+origin.x)/zoom), (int)(y), (int)((getWidth()/2-origin.x)/zoom),(int)(y));
        }
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
}
