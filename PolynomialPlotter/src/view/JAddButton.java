package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;

public class JAddButton extends JComponent implements MouseMotionListener{
    

    private Color bg;
    private Color hoverButtonBgCol;

    private int radius;
    private int padding;
    private int margin;
    private float strokeWidth;
    private Color initialButtonBgCol;
    private Color currentButtonBgCol;
    private Color initialButtonCrossColor;
    private Color currentCrossCol;
    private Color hoverButtonCrossCol;
    private BasicStroke crossStroke;
    


    public JAddButton(Color col){
        super();
        addMouseMotionListener(this);
        radius = 25;
        enableInputMethods(true);
        enableEvents(AWTEvent.ACTION_EVENT_MASK);
        bg = col;
        strokeWidth = 4f;
        initialButtonBgCol = Color.decode("0xC4C4C4");
        hoverButtonBgCol = Color.decode("0xA3A3A3");
        currentButtonBgCol = initialButtonBgCol;
        initialButtonCrossColor = Color.decode("0x5C5C5C");
        currentCrossCol = initialButtonCrossColor;
        hoverButtonCrossCol = Color.decode("0x414141");
        padding = 15;
        margin = 10;
        crossStroke = new BasicStroke(strokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        setPreferredSize(new Dimension(radius*2+margin,radius*2+margin*2));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(bg);
        g2d.drawRect(0, 0, getWidth(), getHeight());
        g2d.translate(getWidth()/2,0);
        g2d.setColor(currentButtonBgCol);
        g2d.fillOval(-radius, margin, radius*2, radius*2);
        g2d.setPaint(currentCrossCol);
        g2d.setStroke(crossStroke);
        g2d.drawLine(-radius+padding,(int)(getHeight()/2),radius-padding,(int)(getHeight()/2));
        g2d.drawLine(0,padding+margin,0,getHeight()-padding-margin);
        
    }

    public boolean isOverButton(MouseEvent e) {
        return e.getPoint().distance(new Point(getWidth()/2,getHeight()/2)) < radius;
    }

    public void callHover(boolean hover){
        if(hover){
            currentButtonBgCol = hoverButtonBgCol;
            currentCrossCol = hoverButtonCrossCol;
        }else{
            currentButtonBgCol = initialButtonBgCol;
            currentCrossCol = initialButtonCrossColor;
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(isOverButton(e) && currentButtonBgCol != hoverButtonBgCol)callHover(true);
        else if(!isOverButton(e) && currentButtonBgCol != initialButtonBgCol)callHover(false);
        
    }




    
}
