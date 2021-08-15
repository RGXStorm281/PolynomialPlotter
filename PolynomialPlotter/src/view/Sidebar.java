package view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import view.FunctionDialog.DialogType;
import view.GUI.FontFamily;
import view.GUI.FontStyle;
import event.IFunctionListener;
import model.IFunction;

/**
 * @author raphaelsack
 */

public class Sidebar extends JPanel {

    private Box vFunctionsBox;
    private JPanel heading;
    private FunctionDialog functionDialog;
    private JAddButton addFunctionButton;
    private StyleClass styleClass;
    private List<IFunctionListener> functionListeners = new ArrayList<IFunctionListener>();
    private List<JFunctionComponent> functionList = new ArrayList<JFunctionComponent>();
    private JLabel headingText;

    public Sidebar(StyleClass styleClass) {
        this.styleClass = styleClass;
        setBackground(this.styleClass.SIDEBAR_BG_COLOR);
        addFunctionButton = new JAddButton(this.styleClass);
        vFunctionsBox = Box.createVerticalBox();
        
        add(vFunctionsBox);
        
        heading = new JPanel(); // Panel used for the Heading-Text (to set a Background)
        headingText = new JLabel("Polynomialplotter");
        headingText.setFont(GUI.getFont(30)); // Set the font to size 30
        headingText.setForeground(this.styleClass.HEADING_FG_COLOR); 
        headingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingText.setBorder(new EmptyBorder(10,50,10,50)); // Add a margin around the Text
        heading.setBackground(this.styleClass.HEADING_BG_COLOR);
        heading.add(headingText);
        vFunctionsBox.add(heading);
        vFunctionsBox.add(addFunctionButton);
        // Remove any Padding
        FlowLayout layout = (FlowLayout)getLayout();
        addFunctionButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
                if(e.getButton() == 1 && addFunctionButton.isOverButton(e)){
                    functionDialog = new FunctionDialog(null, DialogType.ADD, styleClass);
                    functionDialog.setLocationRelativeTo(heading.getParent());
                    for(IFunctionListener functionListener : functionListeners)functionDialog.addFunctionListener(functionListener);
                    functionDialog.start();
                }
                    
            }
        });
        layout.setHgap(0);
        layout.setVgap(0);
    }
    
    /**
     * Aktualisiert die Anzeige der Funktionsliste mit der mitgegebenen Liste.
     * @param functions Die Funktionen, die dem Plotter aktuell zur Verfügung stehen.
     */
    public void updateFunctionComponents(IFunction[] functions){
        functionList.clear();
        for(var function : functions){
            addJFunctionComponent(function);
        }
        renderJFunctionComponents();
    }
    
    /** Fügt ein JFunctionComponent hinzu
     * @param function Das Funktionsobjekt.
     */
    private void addJFunctionComponent(IFunction function){
        JFunctionComponent jfc = new JFunctionComponent(this.styleClass, function.getFunctionName(), function.getDisplayString(), function.getColor());
        jfc.setVisibility(function.isVisible());
        for(IFunctionListener functionListener: functionListeners)jfc.addFunctionListener(functionListener);
        functionList.add(jfc);
    }


    public List<IFunctionListener> getFunctionListeners(){
        return this.functionListeners;
    }

    /**
     * Rendert alle JFunctionComponenten der JFunctionComponent-ArrayList
     */
    private void renderJFunctionComponents(){
        vFunctionsBox.removeAll();
        vFunctionsBox.add(heading);
        vFunctionsBox.add(addFunctionButton);
        for(JFunctionComponent fc: functionList){
            vFunctionsBox.add(fc);
        }
        revalidate();
        repaint();
    }

    
    /** 
     * @return String
     */
    private String getFunction() {
        return functionDialog.getFunctionString();
    }

    
    /** 
     * @return FunctionDialog
     */
    public FunctionDialog getFunctionDialog() {
        return this.functionDialog;
    }

    
    /** fügt ein FunctionListener hinzu
     * @param functionListener
     */
    public void addFunctionListener(IFunctionListener functionListener){
        functionListeners.add(functionListener);
        for(JFunctionComponent jfc: functionList)jfc.addFunctionListener(functionListener);
        
    }

    
    /** Entfernt ein JFunctionComponent von der JFunction-ArrayList und rendert die Components neu
     * @param jFunctionComponent
     */
    public void removeJFunctionComponent(JFunctionComponent jFunctionComponent) {
        functionList.remove(jFunctionComponent);
        jFunctionComponent = null; // Damit sich kein Müll ansammelt.
        renderJFunctionComponents();
    }

    
    /** Färbt die Komponente neu ein
     * @param styleClass
     */
    public void recolor() {
        setBackground(this.styleClass.SIDEBAR_BG_COLOR);
        addFunctionButton.recolor(this.styleClass);
        headingText.setForeground(this.styleClass.HEADING_FG_COLOR); 
        heading.setBackground(this.styleClass.HEADING_BG_COLOR);
        for(JFunctionComponent fc: functionList){
            fc.recolor();
        }

    }

}
