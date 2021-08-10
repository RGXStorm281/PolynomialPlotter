package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

import javax.swing.JComponent;
/**
 * @author raphaelsack
 */

public class JAddButton extends JComponent implements MouseMotionListener {

    private Color bg;
    private Color hoverButtonBgCol;

    private int radius; // Radius des Buttons
    private int padding; // Abstand zwischen Button und Text
    private int margin; // Abstand zwischen Button und Rand
    private float strokeWidth; // StrokeWidth vom "+"-Zeichen

    private Color defaultButtonBgCol;
    private Color currentButtonBgCol;
    private Color defaultButtonCrossColor;
    private Color currentCrossCol;
    private Color hoverButtonCrossCol;
    private BasicStroke crossStroke;

    public JAddButton(StyleClass styleClass) {
        super();
        addMouseMotionListener(this);
        radius = 20;
        strokeWidth = 3f;
        padding = 12;
        margin = 10;
        enableInputMethods(true);
        enableEvents(AWTEvent.ACTION_EVENT_MASK);
        bg = styleClass.SIDEBAR_BG_COLOR;
        defaultButtonBgCol = styleClass.SIDEBAR_ADD_BUTTON_BG;
        hoverButtonBgCol = defaultButtonBgCol.darker();
        currentButtonBgCol = defaultButtonBgCol;
        defaultButtonCrossColor = styleClass.SIDEBAR_ADD_BUTTON_FG;
        currentCrossCol = defaultButtonCrossColor;
        hoverButtonCrossCol = defaultButtonCrossColor.darker();
        crossStroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        setPreferredSize(new Dimension(radius * 2 + margin, radius * 2 + margin * 2));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(bg);
        g2d.drawRect(0, 0, getWidth(), getHeight()); // Hintergrund zeichnen
        g2d.translate(getWidth() / 2, 0); // Zentrieren
        g2d.setColor(currentButtonBgCol);
        g2d.fillOval(-radius, margin, radius * 2, radius * 2);
        g2d.setPaint(currentCrossCol);
        g2d.setStroke(crossStroke);
        g2d.drawLine(-radius + padding, (int) (getHeight() / 2), radius - padding, (int) (getHeight() / 2));
        g2d.drawLine(0, padding + margin, 0, getHeight() - padding - margin);

    }

    /**
     * Checkt, ob die Maus überm Button ist
     * 
     * @param e
     * @return boolean
     */
    public boolean isOverButton(MouseEvent e) {
        // Distanz zwischen Maus und Mitte des Buttons < radius (Button ist ein Kreis)
        return e.getPoint().distance(new Point(getWidth() / 2, getHeight() / 2)) < radius; 
    }

    /**
     * Wird gecallt, wenn die Maus über dem Button ist
     * 
     * @param hover
     */
    public void callHover(boolean hover) {
        if (hover) {
            currentButtonBgCol = hoverButtonBgCol;
            currentCrossCol = hoverButtonCrossCol;
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            currentButtonBgCol = defaultButtonBgCol;
            currentCrossCol = defaultButtonCrossColor;
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isOverButton(e) && currentButtonBgCol != hoverButtonBgCol)
            callHover(true);
        else if (!isOverButton(e) && currentButtonBgCol != defaultButtonBgCol)
            callHover(false);

    }

    /**
     * Färbt die Komponente neu ein
     * 
     * @param styleClass
     */
    public void recolor(StyleClass styleClass) {
        bg = styleClass.SIDEBAR_BG_COLOR;
        defaultButtonBgCol = styleClass.SIDEBAR_ADD_BUTTON_BG;
        defaultButtonCrossColor = styleClass.SIDEBAR_ADD_BUTTON_FG;
        hoverButtonBgCol = defaultButtonBgCol.darker();
        currentButtonBgCol = defaultButtonBgCol;
        currentCrossCol = defaultButtonCrossColor;
        hoverButtonCrossCol = defaultButtonCrossColor.darker();
        repaint();
    }

}
