package view;

import javax.swing.JMenu;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class JCustomMenuBar extends JMenuBar{


    public JCustomMenuBar(){
        JMenu menu = new JMenu("Datei");
        JMenuItem item = new JMenuItem("Aktualisieren");
        menu.add(item);
        add(menu);

    }
    
}
