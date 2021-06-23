package view;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Color;

public class JFunctionContextMenu extends JPopupMenu{
    
    JMenuItem anItem;
    public JFunctionContextMenu(){
        anItem = new JMenuItem("Click Me!");
        anItem.setBackground(Color.BLACK);
        add(anItem);
        setBackground(Color.BLACK);
    }
}
