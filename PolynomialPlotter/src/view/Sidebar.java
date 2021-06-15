package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Sidebar extends JPanel {

    private Box vBox;
    private TF_Sidebar mainField;

    public Sidebar() {
        setBackground(new Color(241,241,241));

        vBox = Box.createVerticalBox();
        add(vBox);

        JPanel heading = new JPanel(); // Panel used for the Heading-Text (to set a Background)
        JLabel headingText = new JLabel("Applikationsname");
        headingText.setFont(GUI.getFont(30)); // Set the font to size 30
        headingText.setForeground(Color.WHITE); 
        headingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingText.setBorder(new EmptyBorder(10,30,10,30)); // Add a margin around the Text
        heading.setBackground(GUI.aktzent1);
        heading.add(headingText);
        vBox.add(heading);
        mainField = new TF_Sidebar(); // Main-Input field to add new things to the list
        vBox.add(mainField);

        // Remove any Padding
        FlowLayout layout = (FlowLayout)getLayout();
        layout.setHgap(0);
        layout.setVgap(0);
    }

    protected void addElement(String text){
        vBox.add(new RowBox(text,Color.BLUE));
        vBox.revalidate();
        vBox.repaint();
    }

	public void onElementRemove(RowBox box) {
        // Gets triggered when an element is removed
	}

}
