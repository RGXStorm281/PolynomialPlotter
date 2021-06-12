package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TF_Sidebar extends JTextField{

    public TF_Sidebar(){
        setFont(GUI.getFont(20));
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals("Eingabe...")) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText("Eingabe...");
                }
            }
            });
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == 10){ // ENTER
                    JTextField tf = (JTextField) e.getSource();
                    Box box = (Box) tf.getParent();
                    box.add(new rowBox(tf.getText(),new Color(24,46,142)));
                    box.revalidate();
                    box.repaint();
                }
            }
        });
        setColumns(10);
        setBorder(BorderFactory.createLineBorder(Color.white));
        setBorder(BorderFactory.createCompoundBorder(
        getBorder(), 
        BorderFactory.createEmptyBorder(10, 20, 10, 10)));
    }
}