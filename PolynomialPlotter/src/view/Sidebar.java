package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.UniversalFunction;
import startup.Settings;
import view.outdated.TF_Sidebar;

public class Sidebar extends JPanel {

    private Box vFunctionsBox;
    private FunctionDialog functionDialog;
    private JAddButton addFunctionButton;

    public Sidebar() {
        setBackground(new Color(241,241,241));
        functionDialog = new FunctionDialog();
        addFunctionButton = new JAddButton(getBackground());
        addFunctionButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(addFunctionButton.isOverButton(e))functionDialog.start();
            }
        });
        vFunctionsBox = Box.createVerticalBox();

        add(vFunctionsBox);

        JPanel heading = new JPanel(); // Panel used for the Heading-Text (to set a Background)
        JLabel headingText = new JLabel("Polynomialplotter");
        headingText.setFont(GUI.getFont(30)); // Set the font to size 30
        headingText.setForeground(Color.WHITE); 
        headingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingText.setBorder(new EmptyBorder(10,30,10,30)); // Add a margin around the Text
        heading.setBackground(GUI.aktzent1);
        heading.add(headingText);
        vFunctionsBox.add(heading);
        vFunctionsBox.add(addFunctionButton);
        // Remove any Padding
        FlowLayout layout = (FlowLayout)getLayout();
        layout.setHgap(0);
        layout.setVgap(0);
    }

    protected void addElement(String text){
        vFunctionsBox.add(new HFunctionBox(text,Color.BLUE));
        vFunctionsBox.revalidate();
        vFunctionsBox.repaint();
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

}
