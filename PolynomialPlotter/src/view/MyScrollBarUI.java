package view;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JButton;




public class MyScrollBarUI extends BasicScrollBarUI{

    private StyleClass styleClass;
    private int thumbOffset = 2;
    
    public MyScrollBarUI(StyleClass styleClass){
        this.styleClass = styleClass;
    }
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(styleClass.SIDEBAR_BG_COLOR);
        g2d.fill(trackBounds);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(styleClass.FUNCTION_CARD_BG);
        g2d.fillRoundRect(thumbBounds.x+thumbOffset, thumbBounds.y, thumbBounds.width-thumbOffset*2, thumbBounds.height, 10, 10);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton decreaseButton = new JButton();
        decreaseButton.setPreferredSize(new Dimension(0,0));
        return decreaseButton;
    }
    
    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton increaseButton = new JButton();
        increaseButton.setPreferredSize(new Dimension(0,0));
        return increaseButton;
    }

}

