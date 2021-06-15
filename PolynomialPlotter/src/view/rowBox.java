package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RowBox extends Box {

    private Color backgroundColor = new Color(200, 200, 200);

    public RowBox(String value, Color color) {
        super(0);
        JTextField tf_main = new JTextField();
        tf_main.setText(value);
        tf_main.setFont(GUI.getFont(20));
        tf_main.setBackground(backgroundColor);
        tf_main.setColumns(10);
        tf_main.addFocusListener(new FocusListener() {
            private String value;
            @Override
            public void focusGained(FocusEvent e) {
                tf_main.setBackground(Color.WHITE);
                tf_main.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                tf_main.setBorder(BorderFactory.createCompoundBorder(tf_main.getBorder(),
                        BorderFactory.createEmptyBorder(10, 20, 10, 10)));
            }

            @Override
            public void focusLost(FocusEvent e) {

                if (tf_main.getText().isEmpty()) {
                    tf_main.setText(this.value);
                } else {
                    this.value = tf_main.getText();
                    tf_main.setBackground(backgroundColor);
                    tf_main.setBorder(BorderFactory.createLineBorder(backgroundColor));
                    tf_main.setBorder(BorderFactory.createCompoundBorder(tf_main.getBorder(),
                            BorderFactory.createEmptyBorder(10, 20, 10, 10)));
                }
            }
        });
        tf_main.setBorder(BorderFactory.createLineBorder(backgroundColor));
        tf_main.setBorder(BorderFactory.createCompoundBorder(tf_main.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 10)));
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color);
        colorPanel.setPreferredSize(new Dimension(45, 50));
        colorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color col;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                this.col = colorPanel.getBackground();
                colorPanel.setBackground(Color.RED);
            }
        
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanel.setBackground(this.col);
            }

            public void mouseClicked(java.awt.event.MouseEvent evt){
                JPanel target = (JPanel) evt.getSource();
                RowBox box = (RowBox) target.getParent();
                Box vBox = (Box) box.getParent();
                ((Sidebar)vBox.getParent()).onElementRemove(box);
                vBox.remove(box);
                vBox.revalidate();
                vBox.repaint();


            }
        });
        add(colorPanel);
        add(tf_main);
    }


}
