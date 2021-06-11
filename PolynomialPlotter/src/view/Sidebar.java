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

    private GUI parent;

    public Sidebar(GUI gui) {
        this.parent = gui;
        setBackground(new Color(241,241,241));

        Box vBox = Box.createVerticalBox();
        add(vBox);

        JPanel heading = new JPanel();
        JLabel headingText = new JLabel("Applikationsname");
        headingText.setFont(GUI.getFont(30));
        heading.setBackground(GUI.aktzent1);
        headingText.setForeground(Color.WHITE);
        headingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingText.setBorder(new EmptyBorder(10,30,10,30));
        heading.add(headingText);
        vBox.add(heading);
        JTextField mainField = new TF_Sidebar();
        vBox.add(mainField);
        vBox.add(new rowBox("29x+2x",new Color(24,46,142),this));
        vBox.add(new rowBox("29x+2x",new Color(24,46,142),this));
        vBox.add(new rowBox("29x+2x",new Color(24,46,142),this));

        // Remove any Padding
        FlowLayout layout = (FlowLayout)getLayout();
        layout.setHgap(0);
        layout.setVgap(0);
    }

}
