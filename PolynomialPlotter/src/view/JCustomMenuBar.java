package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat.Style;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import view.GUI.FontFamily;
import view.GUI.FontStyle;


public class JCustomMenuBar extends JMenuBar{
    private GUI gui;
    private StyleClass styleClass;

    private MouseAdapter ml;

    public JCustomMenuBar(GUI gui, StyleClass styleClass){
        setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        this.gui = gui;
        this.styleClass = styleClass;
        JMenu menu_data = new JMenu("Datei");
        menu_data.setMnemonic('D');
        add(menu_data);
        
        JMenu menu_edit = new JMenu("Bearbeiten");
        menu_edit.setMnemonic('E');
        JMenuItem item_theme = new JMenuItem("Design");
        JMenuItem item_refresh = new JMenuItem("Aktualisieren");
        item_refresh.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.updateTheme();
            }
        });
        item_refresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        menu_edit.add(item_refresh);
        menu_edit.add(item_theme);
        add(menu_edit);
        recolor();
    }

    public  void recolor() { 
        Font font = GUI.getFont(FontFamily.ROBOTO, FontStyle.REGULAR, 15);
        for(Component c: getComponents()){
            if(c instanceof JMenu){
                JMenu m = (JMenu) c;
                m.getPopupMenu().setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                m.getPopupMenu().setBackground(styleClass.MENU_BG);
                m.setBorder(null);
                m.setFont(font);
                for(Component mc: m.getMenuComponents()){
                    if(mc instanceof JMenuItem){
                        JMenuItem mi = (JMenuItem) mc;
                        mi.setBorder(BorderFactory.createEmptyBorder(4,0,4,0));
                        mi.setFont(font.deriveFont(13));
                    }
                }
            }
        }

    }
    
}
