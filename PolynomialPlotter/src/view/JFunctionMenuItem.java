package view;

import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Graphics;
/**
 * @author raphaelsack
 */

public class JFunctionMenuItem extends JMenuItem{

    public JFunctionMenuItem(String label){
        super(label);
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        return;
    }
}
