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

import event.FunctionListener;
import view.FunctionDialog.DialogType;
import view.GUI.FontFamily;
import view.GUI.FontStyle;

public class Sidebar extends JPanel {

    private Box vFunctionsBox;
    private JPanel heading;
    private FunctionDialog functionDialog;
    private JAddButton addFunctionButton;
    private StyleClass styleClass;
    private List<FunctionListener> functionListeners = new ArrayList<FunctionListener>();
    private List<JFunctionComponent> functionList = new ArrayList<JFunctionComponent>();

    public Sidebar(StyleClass styleClass) {
        this.styleClass = styleClass;
        setBackground(this.styleClass.SIDEBAR_BG_COLOR);
        functionDialog = new FunctionDialog(DialogType.ADD);
        addFunctionButton = new JAddButton(this.styleClass);
        addFunctionButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(addFunctionButton.isOverButton(e))functionDialog.start();
            }
        });
        vFunctionsBox = Box.createVerticalBox();

        add(vFunctionsBox);

        heading = new JPanel(); // Panel used for the Heading-Text (to set a Background)
        JLabel headingText = new JLabel("Polynomialplotter");
        headingText.setFont(GUI.getFont(FontFamily.ROBOTO,FontStyle.BOLD,30)); // Set the font to size 30
        headingText.setForeground(this.styleClass.HEADING_FG_COLOR); 
        headingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingText.setBorder(new EmptyBorder(10,50,10,50)); // Add a margin around the Text
        heading.setBackground(this.styleClass.HEADING_BG_COLOR);
        heading.add(headingText);
        vFunctionsBox.add(heading);
        vFunctionsBox.add(addFunctionButton);
        // Remove any Padding
        FlowLayout layout = (FlowLayout)getLayout();
        layout.setHgap(0);
        layout.setVgap(0);
        addJFunctionComponent('g', "g(x) = 4x+3", Color.decode("0x252632"));
        addJFunctionComponent('f', "f(x) = x²+5", Color.decode("0xDE425C"));
        renderJFunctionComponents();
    }

    public void addJFunctionComponent(char functionChar,String functionString, Color functionColor){
        JFunctionComponent jfc = new JFunctionComponent(this.styleClass,functionChar, functionString, functionColor);
        for(FunctionListener functionListener: functionListeners)jfc.addFunctionListener(functionListener);
        functionList.add(jfc);
        // renderJFunctionComponents();
    }

    public void renderJFunctionComponents(){
        vFunctionsBox.removeAll();
        vFunctionsBox.add(heading);
        vFunctionsBox.add(addFunctionButton);
        for(JFunctionComponent fc: functionList){
            vFunctionsBox.add(fc);
        }
        revalidate();
        repaint();
    }

	public void onElementRemove(HFunctionBox box) {
        // Gets triggered when an element is removed
	}

    public String getFunction() {
        return functionDialog.getFunctionString();
    }

    public FunctionDialog getFunctionDialog() {
        return this.functionDialog;
    }

    public void addFunctionListener(FunctionListener functionListener){
        functionListeners.add(functionListener);
        for(JFunctionComponent jfc: functionList)jfc.addFunctionListener(functionListener);
        functionDialog.addFunctionListener(functionListener);
    }

    public void removeJFunctionComponent(JFunctionComponent jFunctionComponent) {
        functionList.remove(jFunctionComponent);
        renderJFunctionComponents();
    }

}
