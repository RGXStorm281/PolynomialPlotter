package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.plaf.FontUIResource;

import event.FunctionEvent;
import event.FunctionVisibilityToggledEvent;
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
import event.IFunctionListener;
import model.Koordinate;
import model.Tuple;
/**
 * @author raphaelsack
 */

public class JFunctionComponent extends JComponent implements MouseMotionListener {

    private String functionString; // Ganzer Funktions-String
    private String functionChar; // Nur der Name

    private StyleClass styleClass;
    private FunctionDialog functionEditDialog;
    
    
    private float cardWidthPercent; // Wie viel Prozent der Sidebar-Breite soll die Karte haben?
    private float cardWidth; // So Breit ist die Karte (wird in paintComponent berechnet)
    private float cardVMargin; // Vertical-Margin -> Vertikaler Abstand zwischen Rand und Karte
    private int cardHMargin; // Horizontal-Margin -> Horizontaler Abstand zwischen Rand und Karte

    private int circleRadius; // Radius des Color-Circles
    private int circleMargin; // Margin des Kreises

    private int closeButtonMargin;
    private int closeButtonRadius;
    
    private float closeButtonCrossLenghtFactor; // Wie lang soll das Kreuz, relativ zum Kreis sein? (Beim Close-Button)
    private float closeButtonStrokeWidth;

    
    
    private Color currentCardBg;
    private Color defaultCardBg;
    private Color hoverCardBg;

    private Color circleColor;

    private List<IFunctionListener> functionListeners = new ArrayList<IFunctionListener>();

    private Color closeButtonCrossDefaultColor;
    private Color closeButtonCrossCurrentColor;
    private Color closeButtonCrossHoverColor;
    private Color closeButtonBgDefaultColor;
    private Color closeButtonBgCurrentColor;
    private Color closeButtonBgHoverColor;
    private BasicStroke closeButtonCrossStroke;
    private GeneralPath closeCrossPath;

    private Color functionStringColor;

    private boolean isVisible = true;
    
    public JFunctionComponent(StyleClass styleClass, String functionChar, String functionString, Color circleColor) {
        super();
        this.circleColor = circleColor;
        this.functionString = functionString;
        this.functionChar = functionChar;
        this.styleClass = styleClass;
        functionStringColor = styleClass.FUNCTION_CARD_FG;
        
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
        closeButtonCrossStroke = new BasicStroke(closeButtonStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        closeButtonCrossLenghtFactor = 0.5f;
        
        setPreferredSize(new Dimension(getWidth(), 80));
        addMouseListener(new MouseAdapter() {


            @Override
            public void mouseExited(MouseEvent e) {
                resetAllStyles();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isInCardBounds(e)) {
                    if (isInCloseButtonBounds(e)) {
                        for (IFunctionListener listener : functionListeners)
                        ((IFunctionListener) listener).functionDeleted(
                            new FunctionEvent(e.getSource(), circleColor, functionString, functionChar));
                            destroy();
                            return;
                        }
                    if (isInCircleButtonBounds(e)) {
                        var toggleVisibleEvent = new FunctionVisibilityToggledEvent(e.getSource(), circleColor, functionString, functionChar);
                        for(IFunctionListener listener : functionListeners){
                            listener.functionVisibilityToggled(toggleVisibleEvent);
                        }
                        repaint();
                        return;
                    }
                    JFunctionComponent t = (JFunctionComponent)(e.getSource());
                    functionEditDialog = new FunctionDialog(t,DialogType.EDIT,styleClass);
                    functionEditDialog.setFunctionString(t.functionString);
                    functionEditDialog.setColor(t.circleColor);
                    functionEditDialog.setLastFunctionChar(t.functionChar);
                    for(IFunctionListener listener : functionListeners)functionEditDialog.addFunctionListener(listener);
                    functionEditDialog.setLocationRelativeTo(t);
                    functionEditDialog.start();
                }
            }
        });
            
        }
        
        /**
         * Enternt sich selbst aus seinem Parent und löscht das Objekt
         */
        protected void destroy() {
            ((Sidebar) getParent().getParent()).removeJFunctionComponent(this);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (cardHMargin == 0.0f) cardHMargin = (int) (getWidth() * ((1 - cardWidthPercent) / 2)); // Horizontal-Margin = Der Rest der Breite (ohne der Karte) / 2. 
            if(cardWidth == 0.0f) cardWidth = (int) (getWidth() * cardWidthPercent);
            addMouseMotionListener(this);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
            g2d.setColor(isVisible ? currentCardBg : currentCardBg.darker());
            g2d.fillRoundRect((int) (cardHMargin), (int) (cardVMargin), (int) (cardWidth),
            (int) (getHeight() - cardVMargin * 2), 20, 20);
            paintColorCircle(g2d);
            paintFunctionString(g2d);
            paintCloseButton(g2d);
            // paintEditButton(g2d);
        }
       
        /**
         * malt den Close Button
         * 
         * @param g2d
         */
        private void paintCloseButton(Graphics2D g2d) {
            g2d.translate(0, -getHeight() / 2);
            Koordinate buttonCenter = new Koordinate(cardHMargin + closeButtonMargin, cardVMargin + closeButtonMargin);
            if (closeCrossPath == null) { // Speichert einen Pfad nach dem ersten berechnen ab (Spart etwas Rechenzeit ein)

                // Nach: e^i*a = (sin(a)+cos(a)i)
                /**
                 * Mit x = sin(a) * r und y = cos(a) * r bekommt man einen Punkt auf einem Kreis (mit radius = r), die Position auf dem Kreis wird mit Radianten angegeben (a).
                 * Über einen Offset lässt sich dieser Punkt frei bewegen.
                 * z.B. x1
                 *  sin(0.25*pi) --> x Position auf dem Einheitskreis beim Radianten 0.25*pi (45 Grad)
                 *  (buttonRadius*LengthFactor) --> Länge der Linie
                 *  buttonCenter.x + radius --> Offset
                 * 
                 * Da PI = 180Grad ist, nehmen wir für eine Linie immer einen Punkt auf dem Kreis welcher 45 Grad in eine Richtung rotiert ist und Verbinden ihn mit einem Punkt, welcher 180-Grad = PI weiter auf dem Kreis rotiert um eine Diagonale zu erhalten.
                 * Wenn man das dann 2 mal macht, hat mein ein Kreuz mit einer insgesamten 45° Drehung
                 */
                closeCrossPath = new GeneralPath();
                int x1 = (int) (Math.sin(0.25 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getX() + closeButtonRadius);
                int y1 = (int) (Math.cos(0.25 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getY() + closeButtonRadius);
                int x2 = (int) (Math.sin(1.25 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getX() + closeButtonRadius);
                int y2 = (int) (Math.cos(1.25 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getY() + closeButtonRadius);
                closeCrossPath.moveTo(x1, y1);
                closeCrossPath.lineTo(x2, y2);
                int x3 = (int) (Math.sin(0.75 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getX() + closeButtonRadius);
                int y3 = (int) (Math.cos(0.75 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getY() + closeButtonRadius);
                int x4 = (int) (Math.sin(1.75 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getX() + closeButtonRadius);
                int y4 = (int) (Math.cos(1.75 * Math.PI) * (closeButtonRadius * closeButtonCrossLenghtFactor) + buttonCenter.getY() + closeButtonRadius);
                closeCrossPath.moveTo(x3, y3);
                closeCrossPath.lineTo(x4, y4);
            }
            g2d.setColor(closeButtonBgCurrentColor);
            g2d.fillOval((int) buttonCenter.getX(), (int) buttonCenter.getY(), closeButtonRadius * 2,
            closeButtonRadius * 2);
            g2d.setStroke(closeButtonCrossStroke);
            g2d.setPaint(closeButtonCrossCurrentColor);
            g2d.draw(closeCrossPath);
            
        }
        
        public void setFunctionString(String functionString){
            this.functionString = functionString;
            revalidate();
            repaint();
        }
    
        
        /**
         * Malt den Funktions-String
     * 
     * @param g2d
     */
    private void paintFunctionString(Graphics2D g2d) {
        g2d.setColor(isVisible ? functionStringColor : functionStringColor.darker());
        g2d.setFont(GUI.getFont(20));
        String visibleString = functionString;
        boolean overflow = false;
        double height = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), "f").getVisualBounds().getHeight();
        int x = (int) (getColorCirclePosition().getItem1() + circleRadius * 2 + circleMargin);
        int y = (int) (height/2);
        int width = g2d.getFontMetrics().stringWidth(visibleString);
        while(x+width >= getWidth()){
            width = g2d.getFontMetrics().stringWidth(visibleString);
            visibleString = visibleString.substring(0, visibleString.length()-1);
            overflow = true;
        }
        if(overflow) visibleString+="...";
        g2d.drawString(GUI.decorate(visibleString),x,y);
    }

    /**
     * Malt den Farb-Kreis
     * 
     * @param g2d
     */
    private void paintColorCircle(Graphics2D g2d) {
        g2d.translate(0, getHeight()/2); // In die Mitte der Höhe zentrieren um den Kreis horizontal zu zentrieren
        g2d.setColor(isVisible? circleColor : currentCardBg);
        var position = getColorCirclePosition();
        g2d.fillOval(position.getItem1(), position.getItem2(),
        circleRadius * 2, circleRadius * 2);
        g2d.setColor(isVisible ? circleColor.darker() : currentCardBg.brighter());
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(position.getItem1(), position.getItem2(),
                circleRadius * 2, circleRadius * 2);

    }
    
    /**
     * Gibt die position für den farbigen Kreis relativ von der Mitte zurück.
     * @return Die Positionskordinaten (negaive Werte)
     */
    private Tuple<Integer,Integer> getColorCirclePosition(){
        var positionX = (int) (cardHMargin + closeButtonMargin * 2 + closeButtonRadius * 2);
        var positionY = (int) (-circleRadius);
        return new Tuple<>(positionX, positionY);
    }

    /**
     * Wird benötigt um den Style zu resetten, falls ein MouseMoved Event zu langsam
     * war und einen Style nicht resetten konnte
     */
    protected void resetAllStyles() {
        currentCardBg = defaultCardBg;
        closeButtonBgCurrentColor = closeButtonBgDefaultColor;
        closeButtonCrossCurrentColor = closeButtonCrossDefaultColor;
        setCursor(Cursor.getDefaultCursor());
        repaint();
    }

  
    /**
     * Prüft ob die Maus momentan die Karte berührt
     * 
     * @param e
     * @return boolean
     */
    private boolean isInCardBounds(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // Ist in Card-Bereich wenn x kleiner als die Breite-HMargin, aber größer als HMargin. (Das gleiche mit VMargin und Höhe bei y)
        return (x > cardHMargin && x < cardHMargin + cardWidth) && (y > cardVMargin && y < getHeight() - cardVMargin);
    }

    /**
     * Wird gecallt, wenn die Maus über der Karte hovert.
     * 
     * @param hover
     */
    public void callCardHover(boolean hover) {
        if (hover) {
            currentCardBg = hoverCardBg;

        } else {
            currentCardBg = defaultCardBg;
        }
        repaint();
    }

    /**
     * Wird gecallt, wenn die Maus auf der Komponente gedraggt wurde
     * 
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Wird gecallt, wenn die Maus auf der Komponente bewegt wurde
     * 
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (isInCardBounds(e)) {
            if (currentCardBg != hoverCardBg)
                callCardHover(true);
            if (isInCloseButtonBounds(e) && closeButtonBgCurrentColor != closeButtonBgHoverColor)
                callCloseButtonHover(true);
            else if (!isInCloseButtonBounds(e) && closeButtonBgCurrentColor != closeButtonBgDefaultColor)
                callCloseButtonHover(false);
        } else if (!isInCardBounds(e) && currentCardBg != defaultCardBg) {
            resetAllStyles();
        }
    }

    /**
     * wird gecallt, wenn die Maus überm Close-Button hovert.
     * 
     * @param hover
     */
    private void callCloseButtonHover(boolean hover) {
        if (hover) {
            closeButtonBgCurrentColor = closeButtonBgHoverColor;
            closeButtonCrossCurrentColor = closeButtonCrossHoverColor;
            setCursor(new Cursor(Cursor.HAND_CURSOR));

        } else {
            closeButtonBgCurrentColor = closeButtonBgDefaultColor;
            closeButtonCrossCurrentColor = closeButtonCrossDefaultColor;
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

  

    /**
     * Gibt an ob die Maus momentan über dem Close-Button der Komponente ist
     * 
     * @param e MouseEvent
     * @return boolean
     */
    private boolean isInCloseButtonBounds(MouseEvent e) {
        return e.getPoint()
                .distance(new Point(
                        (int) (getWidth() * ((1 - cardWidthPercent) / 2) + closeButtonMargin + closeButtonRadius),
                        (int) cardVMargin + closeButtonMargin + closeButtonRadius)) < closeButtonRadius;
    }
    
    /**
     * Gibt an ob die Maus momentan über dem farbigen Kreis ist.
     * @param e Das Maus-Event mit der aktuellen Position.
     * @return True, wenn die Maus über dem Kreis ist.
     */
    private boolean isInCircleButtonBounds(MouseEvent e) {
        var colorCirclePosition = getColorCirclePosition();
        int x = (int) (colorCirclePosition.getItem1() + circleRadius);
        int y = (int) (colorCirclePosition.getItem2() + (getHeight() / 2) + circleRadius);
        var circleCenter = new Point(x, y);
        
        // Treffer wenn der Punkt auf den geklickt wurde nicht weiter als der Radius vom Zentrum entfernt liegt.
        var distance = e.getPoint().distance(circleCenter);
        return distance < circleRadius;
    }

    /**
     * Fügt einen FunctionListener hinzu
     * 
     * @param functionListener
     */
    public void addFunctionListener(IFunctionListener functionListener) {
        functionListeners.add(functionListener);
    }

    public void setVisibility(boolean b){
        this.isVisible = b;
    }

    public boolean getVisibility(){
        return this.isVisible;
    }

    /**
     * Färbt die Komponente neu ein
     * 
     * @param styleClass
     */
    public void recolor() {
        functionStringColor = styleClass.FUNCTION_CARD_FG;
        defaultCardBg = styleClass.FUNCTION_CARD_BG;
        hoverCardBg = styleClass.FUNCTION_CARD_BG_HOVER;
        closeButtonBgDefaultColor = styleClass.FUNCTION_CARD_CLOSE_BUTTON_BG;
        closeButtonCrossDefaultColor = styleClass.FUNCTION_CARD_CLOSE_BUTTON_FG;
        currentCardBg = defaultCardBg;
        closeButtonBgHoverColor = closeButtonBgDefaultColor.darker();
        closeButtonBgCurrentColor = closeButtonBgDefaultColor;
        closeButtonCrossHoverColor = closeButtonCrossDefaultColor.darker();
        closeButtonCrossCurrentColor = closeButtonCrossDefaultColor;
        repaint();   
    }

    public static Color LerpRGB(Color a, Color b, float t) {
        // Reference: https://www.alanzucconi.com/2016/01/06/colour-interpolation/
        int red = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int green = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int blue = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        int alpha = (int) (a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t);
        return new Color(red, green, blue, alpha);
    }

    public void editFunction(String fs, Color functionColor) {
        this.functionString = fs;
        this.circleColor = functionColor;
        System.out.println(this.functionString);
        revalidate();
        repaint();
    }

}