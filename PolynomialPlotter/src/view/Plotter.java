package view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Plotter extends JPanel {

    public Plotter() {
        super();
        setBackground(Color.WHITE);
        JButton button = new JButton("TEST");
        add(button);
    }
}
