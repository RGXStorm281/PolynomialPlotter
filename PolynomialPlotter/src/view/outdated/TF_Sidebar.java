package view.outdated;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JTextField;

import view.GUI;
import view.Sidebar;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TF_Sidebar extends JTextField{

    public TF_Sidebar(){
    //     setFont(GUI.getFont(20));
    //     addFocusListener(new FocusListener() {
    //         @Override
    //         public void focusGained(FocusEvent e) {
    //             if (getText().equals("Eingabe...")) {
    //                 setText("");
    //                 setForeground(Color.BLACK);
    //             }
    //         }
    //         @Override
    //         public void focusLost(FocusEvent e) {
    //             if (getText().isEmpty()) {
    //                 setForeground(Color.GRAY);
    //                 setText("Eingabe...");
    //             }
    //         }
    //         });
    //     addKeyListener(new KeyAdapter(){
    //         @Override
    //         public void keyPressed(KeyEvent e){
    //             if(e.getKeyCode() == 10){ // ENTER
    //                 TF_Sidebar tf = (TF_Sidebar) e.getSource();
    //                 ((Sidebar)((Box)tf.getParent()).getParent()).addElement(tf.getText());
    //                 tf.setText("");
    //             }
    //         }
    //     });
    //     setColumns(10);
    //     setForeground(Color.GRAY);
    //     setBorder(BorderFactory.createLineBorder(Color.white));
    //     setText("Eingabe...");
    //     setBorder(BorderFactory.createCompoundBorder(
    //     getBorder(), 
    //     BorderFactory.createEmptyBorder(10, 20, 10, 10)));
    }
}