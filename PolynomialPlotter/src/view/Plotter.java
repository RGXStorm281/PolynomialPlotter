package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;

public class Plotter extends JPanel {

    private float zoom = 1f;
    private float spacing = 20;
    private Point origin;
    private Point mousePt;


    public Plotter(){
        addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
               handleMouseWheelMoved(e);
			}
        });
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
        addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
                Plotter self = (Plotter) e.getSource();
                float dx = e.getX() - mousePt.x;
                float dy = e.getY() - mousePt.y;
                self.translate(dx,dy);
                origin.setLocation(origin.x + dx, origin.y + dy);
                mousePt = e.getPoint();
                System.out.println(origin.x + " " + origin.y);
                repaint();
			}
        });
        origin = new Point(getWidth()/2,getHeight()/2);
    }

    protected void handleMouseWheelMoved(MouseWheelEvent e) {
         // Down = 1, Up = -1
         Plotter self = (Plotter) e.getSource();
         if(e.isControlDown()){
             self.zoom-=e.getWheelRotation();
         }else{
             self.zoom-=e.getWheelRotation()*0.1;
         }
         if(self.zoom<=0.1)self.zoom=0.1f;
         self.repaint();
         System.out.println(self.getZoom());
	}

	public void paint(Graphics g){
        int width = this.getWidth();
        int height = this.getHeight();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,width,height);
        g2d.translate(width/2,height/2);
        
        g2d.translate((double) origin.x,(double)origin.y);
        g2d.scale(zoom,zoom);
        drawGrid(g2d);
        drawAxes(g2d);
        drawFunction(g2d);
    }

    private void drawFunction(Graphics2D g2d) {

        GeneralPath gp = new GeneralPath();
        g2d.setPaint(Color.RED);
        gp.moveTo(getWidth()/2, getHeight()/2);
        gp.lineTo(0,0);
        g2d.draw(gp);

	}

	private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        float xCorrection = 0;
        float yCorrection = 0;
        g2d.drawLine((int)(0-origin.x/zoom), (int)((getHeight()/2-yCorrection)/zoom), (int)((getWidth()-origin.x)/zoom),(int)((getHeight()/2-yCorrection)/zoom));
        g2d.drawLine((int)((getWidth()/2-xCorrection)/zoom), (int)((0-origin.y)/zoom), (int)((getWidth()/2-xCorrection)/zoom),  (int)((getHeight()-origin.y)/zoom));
	}

	private void drawOrientation(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawOval(0,0, 100,100);
	}

	public void drawGrid(Graphics2D g2d){
        int width = this.getWidth();
        int height = this.getHeight();
        g2d.setColor(new Color(200,200,200));
        for(float x = (-origin.x+(origin.x%(spacing*zoom)))/zoom;x<(width-origin.x+(origin.x%(spacing*zoom)))/zoom;x+=spacing){
            g2d.drawLine((int)(x),(int)((0-origin.y)/zoom),(int)(x),(int)((height-origin.y)/zoom));
        }
        for(float y = (-origin.y+(origin.y%(spacing*zoom)))/zoom;y<(height-origin.y+(origin.y%(spacing*zoom)))/zoom;y+=spacing){
            g2d.drawLine((int)((0-origin.x)/zoom),(int)(y),(int)((width-origin.x)/zoom),(int)(y));
        }
    }

    public void setZoom(float zoom){
        this.zoom = zoom;
        this.repaint();
    }

    public float getZoom(){
        return this.zoom;
    }

    public void translate(float x, float y){
        
    }

}
