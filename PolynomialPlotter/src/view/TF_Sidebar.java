package view;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
        setColumns(10);
        setBorder(BorderFactory.createLineBorder(Color.white));
        setBorder(BorderFactory.createCompoundBorder(
        getBorder(), 
        BorderFactory.createEmptyBorder(10, 20, 10, 10)));
    }
}