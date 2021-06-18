package events.plot;

import java.awt.event.MouseListener;

import javax.swing.JPanel;

import logic.BusinessLogic;
import view.GUI;

import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Point;

public class PlotMouseListener implements MouseListener {

    private BusinessLogic logic;

    public PlotMouseListener(GUI view, BusinessLogic logic) {
        this.logic = logic;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Mouse Point speichern für Drag logik in PlotMouseDragged.java
        Point mousePt = e.getPoint();
        logic.setMousePoint(mousePt);
        ((JPanel) e.getSource()).setCursor(new Cursor(Cursor.MOVE_CURSOR));
        System.out.println("EVENT TRIGGERED: Plotter Mouse Pressed");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ((JPanel) e.getSource()).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ((JPanel) e.getSource()).setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ((JPanel) e.getSource()).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

}
