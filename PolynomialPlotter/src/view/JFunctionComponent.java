package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Point;


import javax.swing.JComponent;

import event.FunctionEvent;
import event.FunctionListener;
import view.FunctionDialog.DialogType;
import view.GUI.FontFamily;
import view.GUI.FontStyle;

import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JFunctionComponent extends JComponent implements MouseMotionListener {

    private String functionString;
    private char functionChar;

    private FunctionDialog functionDialog;

    private Color currentCardBg;
    private Color defaultCardBg;
    private Color hoverCardBg;
    private float cardWidthPercent;
    private float cardWidth;
    private float cardVMargin;
    
    private Color circleColor;
    private int circleRadius;
    private int circleMargin;

    private List<FunctionListener> functionListeners = new ArrayList<FunctionListener>();

    private Color closeButtonCrossDefaultColor;
    private Color closeButtonCrossCurrentColor;
    private Color closeButtonCrossHoverColor;
    private Color closeButtonBgDefaultColor;
    private Color closeButtonBgCurrentColor;
    private Color closeButtonBgHoverColor;
    private int closeButtonMargin;
    private int closeButtonRadius;
    private BasicStroke closeButtonCrossStroke;
    private float closeButtonStrokeWidth;
    private float closeButtonCrossLenghtFactor;
    private GeneralPath closeCrossPath;

    private Color editButtonCurrentColor;
    private Color editButtonDefaultColor;
    private Color editButtonHoverColor;
    private int editButtonCircleRadii;
    private int editButtonCircleMargin;

    private int cardMargins; // Margin left and right to the border


    public JFunctionComponent(StyleClass styleClass, char functionChar, String functionString, Color circleColor) {
        super();
        this.circleColor = circleColor;
        this.functionString = functionString;
        this.functionChar = functionChar;

        functionDialog = new FunctionDialog(DialogType.EDIT);
        functionDialog.setFunctionString(functionString);
        functionDialog.setColor(circleColor);
        functionDialog.setLastFunctionChar(this.functionChar);

        defaultCardBg = styleClass.FUNCTION_CARD_BG;
        currentCardBg = defaultCardBg;
        hoverCardBg = styleClass.FUNCTION_CARD_BG_HOVER;
        cardWidthPercent = .9f;
        cardVMargin = 7;

        circleRadius = 17;
        circleMargin = 16;

        closeButtonBgDefaultColor = styleClass.FUNCTION_CARD_CLOSE_BUTTON_BG;
        closeButtonBgHoverColor = closeButtonBgDefaultColor.darker();
        closeButtonBgCurrentColor = closeButtonBgDefaultColor;
        closeButtonCrossDefaultColor = styleClass.FUNCTION_CARD_CLOSE_BUTTON_FG;
        closeButtonCrossHoverColor = closeButtonCrossDefaultColor.darker();
        closeButtonCrossCurrentColor = closeButtonCrossDefaultColor;

        closeButtonRadius = 7;
        closeButtonMargin = 8;
        closeButtonStrokeWidth = 1.5f;
        closeButtonCrossStroke = new BasicStroke(closeButtonStrokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        closeButtonCrossLenghtFactor = 0.5f;

        editButtonDefaultColor = styleClass.FUNCTION_CARD_EDIT_BUTTON_BG;
        editButtonCurrentColor = editButtonDefaultColor;
        editButtonHoverColor = editButtonDefaultColor.darker();
        editButtonCircleRadii = 2;
        editButtonCircleMargin = 2;
        setPreferredSize(new Dimension(getWidth(), 80));
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseExited(MouseEvent e) {
                resetAllStyles();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isInCloseButtonBounds(e)){
                            for(FunctionListener listener: functionListeners)((FunctionListener)listener).functionDeleted(new FunctionEvent(e.getSource(), circleColor, functionString, functionChar)); 
                            destroy();
                }else if(isInEditButtonBounds(e)){
                    functionDialog.start();
                }
            }
        });
    }

    protected void destroy() {
        ((Sidebar)getParent().getParent()).removeJFunctionComponent(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(cardMargins == 0.0f) cardMargins = (int)(getWidth() * ((1 - cardWidthPercent) / 2));
        addMouseMotionListener(this);
        cardWidth = (int) (getWidth() * cardWidthPercent);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        // g2d.drawRect(0,0,getWidth()-1,getHeight()-1);
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.setColor(currentCardBg);
        g2d.fillRoundRect((int) (-cardWidth / 2), (int) (-getHeight() / 2 + cardVMargin), (int) (cardWidth),
        (int) (getHeight() - cardVMargin*2),20,20);
        paintColorCircle(g2d);
        paintFunctionString(g2d);
        paintCloseButton(g2d);
        paintEditButton(g2d);
    }

    private void paintEditButton(Graphics2D g2d) {
        int rectY = (int)(cardVMargin+editButtonCircleMargin*3.5);
        int width = (int)(2*(3*editButtonCircleRadii+editButtonCircleMargin));
        int rectX = (int)(getWidth() - cardMargins - editButtonCircleMargin*3.5 - width);
        g2d.setColor(editButtonCurrentColor);
        for(int i = 0;i<3;i++){
            g2d.fillOval(rectX+(i*(editButtonCircleMargin+editButtonCircleRadii*2)), rectY, editButtonCircleRadii*2, editButtonCircleRadii*2);
        }

    }

    private void paintCloseButton(Graphics2D g2d) {
        g2d.translate(-getWidth() / 2, -getHeight() / 2);
        if(closeCrossPath == null){
            closeCrossPath = new GeneralPath();
        System.out.println("redo path");
        int x1 = (int)(Math.sin(0.25*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardMargins+closeButtonMargin+closeButtonRadius);
        int y1 = (int)(Math.cos(0.25*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardVMargin+closeButtonMargin+closeButtonRadius);
        int x2 = (int)(Math.sin(1.25*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardMargins+closeButtonMargin+closeButtonRadius);
        int y2 = (int)(Math.cos(1.25*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardVMargin+closeButtonMargin+closeButtonRadius);
        closeCrossPath.moveTo(x1,y1);
        closeCrossPath.lineTo(x2,y2);
        int x3 = (int)(Math.sin(0.75*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardMargins+closeButtonMargin+closeButtonRadius);
        int y3 = (int)(Math.cos(0.75*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardVMargin+closeButtonMargin+closeButtonRadius);
        int x4 = (int)(Math.sin(1.75*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardMargins+closeButtonMargin+closeButtonRadius);
        int y4 = (int)(Math.cos(1.75*Math.PI) * closeButtonRadius*closeButtonCrossLenghtFactor + cardVMargin+closeButtonMargin+closeButtonRadius);
        closeCrossPath.moveTo(x3,y3);
        closeCrossPath.lineTo(x4,y4);
    }
    g2d.setColor(closeButtonBgCurrentColor);
    g2d.fillOval(cardMargins+closeButtonMargin, (int)cardVMargin+closeButtonMargin, closeButtonRadius*2, closeButtonRadius*2);
    g2d.setStroke(closeButtonCrossStroke);
    g2d.setPaint(closeButtonCrossCurrentColor);
    g2d.draw(closeCrossPath);

    }

    private void paintFunctionString(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(GUI.getFont(FontFamily.ROBOTO,FontStyle.REGULAR,20));
        double height = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(),"f").getVisualBounds().getHeight();
        g2d.drawString(functionString,(int)(-cardWidth / 2 + closeButtonRadius*2 + 2*circleMargin + circleRadius*2),(int)(height/2));
    }

    private void paintColorCircle(Graphics2D g2d) {
        g2d.setColor(circleColor);
        g2d.fillOval((int)(-cardWidth / 2 + closeButtonMargin*2 + closeButtonRadius*2), (int)(-circleRadius), circleRadius*2, circleRadius*2);


    }

    protected void resetAllStyles() {
        currentCardBg = defaultCardBg;
        closeButtonBgCurrentColor = closeButtonBgDefaultColor;
        closeButtonCrossCurrentColor = closeButtonCrossDefaultColor;
        editButtonCurrentColor = editButtonDefaultColor;
        repaint();
    }

    private boolean isInEditButtonBounds(MouseEvent e){
        // Trigger-Box is bigger than the actual Buttons
        int rectY = (int)(cardVMargin);
        int width = (int)(2*(3*editButtonCircleRadii+editButtonCircleMargin));
        int rectX = (int)(getWidth() - cardMargins - editButtonCircleMargin*3.5 - width);
        int height = (int)(4*(editButtonCircleRadii+editButtonCircleMargin));
        return (e.getX() > rectX && e.getX() < rectX+width) && (e.getY() > rectY && e.getY() < rectY+height);
    }

    private boolean isInCardBounds(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        return (x > cardMargins && x < cardMargins + cardWidth) && (y > cardVMargin && y < getHeight() - cardVMargin);
    }

    public void callCardHover(boolean hover){
        if(hover){
            currentCardBg = hoverCardBg;
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
        }else{
            currentCardBg = defaultCardBg;      
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isInCardBounds(e)){
            if(currentCardBg != hoverCardBg)callCardHover(true);
            if(isInCloseButtonBounds(e) && closeButtonBgCurrentColor != closeButtonBgHoverColor)callCloseButtonHover(true);
            else if(!isInCloseButtonBounds(e) && closeButtonBgCurrentColor != closeButtonBgDefaultColor)callCloseButtonHover(false);
            if(isInEditButtonBounds(e) && editButtonCurrentColor != editButtonHoverColor)callEditButtonHover(true);
            else if(!isInEditButtonBounds(e) && editButtonCurrentColor != editButtonDefaultColor)callEditButtonHover(false);
        }
        else if (!isInCardBounds(e)&&currentCardBg != defaultCardBg){
            resetAllStyles();
        }
    }

    private void callCloseButtonHover(boolean hover) {
        if(hover){
            closeButtonBgCurrentColor = closeButtonBgHoverColor;
            closeButtonCrossCurrentColor = closeButtonCrossHoverColor;
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
        }else{
            closeButtonBgCurrentColor = closeButtonBgDefaultColor;      
            closeButtonCrossCurrentColor = closeButtonCrossDefaultColor;      
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }
    
    private void callEditButtonHover(boolean hover) {
        if(hover){
            editButtonCurrentColor = editButtonHoverColor;
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
        }else{
            editButtonCurrentColor = editButtonDefaultColor;           
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }
    private boolean isInCloseButtonBounds(MouseEvent e) {
        return e.getPoint().distance(new Point((int)(getWidth() * ((1 - cardWidthPercent) / 2) + closeButtonMargin + closeButtonRadius), (int)cardVMargin+closeButtonMargin+closeButtonRadius)) < closeButtonRadius;
    }

    public void addFunctionListener(FunctionListener functionListener) {
        functionDialog.addFunctionListener(functionListener);
        functionListeners.add(functionListener);
    }

    
}