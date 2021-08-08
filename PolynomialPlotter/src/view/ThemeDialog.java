package view;

import java.awt.BorderLayout;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import view.GUI.FontFamily;
import view.GUI.FontStyle;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * @author raphaelsack
 */

public class ThemeDialog extends JFrame{

    private Color buttonBackground;
    private Color buttonForeground;

    private StyleClass styleClass;

    private JButton button;
    private JLabel label;
    private JComboBox<String> jcb;

    public ThemeDialog(GUI gui, StyleClass styleClass){
        buttonBackground = styleClass.BUTTON_BG;
        this.styleClass = styleClass;
        buttonForeground = styleClass.BUTTON_FG;
        Font font = GUI.getFont(16);
        getContentPane().setBackground(styleClass.DIALOG_BG);
        MouseAdapter buttonAdapter = new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(buttonBackground.darker());
                button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(buttonBackground.darker()),BorderFactory.createEmptyBorder(10,10,10,10)));
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(buttonBackground);
                button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(buttonBackground),BorderFactory.createEmptyBorder(10,10,10,10)));
            }
        };
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Border b = BorderFactory.createEmptyBorder(10,10,10,10);
        InputStream in = getClass().getResourceAsStream("data/icon.png"); 
        try {
            setIconImage(new ImageIcon(in.readAllBytes()).getImage());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Box hBox = Box.createHorizontalBox();
        label = new JLabel("Farb-Schema:");
        label.setBorder(b);
        label.setFont(font.deriveFont(15f));
        String[] files = {"darkTheme","dcdark","dclight","spot"};
        jcb = new JComboBox<String>(files);
        jcb.setFont(font.deriveFont(15f));
        jcb.setForeground(styleClass.DIALOG_FG);
        jcb.setBackground(styleClass.DIALOG_BG);

        jcb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,0,10),BorderFactory.createLineBorder(styleClass.DIALOG_BG)));
        button = new JButton("Anwenden");
        button.setBackground(buttonBackground);
        button.setForeground(buttonForeground);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(buttonBackground),BorderFactory.createEmptyBorder(10,10,10,10)));
        button.setFont(font.deriveFont(15f));
        button.addMouseListener(buttonAdapter);
        button.setOpaque(true);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String theme = (String) jcb.getSelectedItem();
                gui.changeTheme((String)(theme));
                Properties propertiesFile = new Properties();
                try {
                    InputStream in = (getClass().getResourceAsStream("../startup/settings.properties"));
                    propertiesFile.load(new BufferedReader(new InputStreamReader(in)));
                    propertiesFile.setProperty("theme", jcb.getSelectedItem()+"");
                    propertiesFile.store(new FileOutputStream(getClass().getResource("../startup/settings.properties").getPath()),null);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        });
        hBox.add(label);
        hBox.add(jcb);
        hBox.add(button);
        hBox.setBorder(b);
        getContentPane().add(hBox,BorderLayout.CENTER);
        pack();
        setResizable(false);
    
    }

    public void recolor(){
        buttonBackground = styleClass.BUTTON_BG;
        buttonForeground = styleClass.BUTTON_FG;
        button.setBackground(buttonBackground);
        button.setForeground(buttonForeground);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(buttonBackground),BorderFactory.createEmptyBorder(10,10,10,10)));
        jcb.setForeground(styleClass.DIALOG_FG);
        jcb.setBackground(styleClass.DIALOG_BG);
        jcb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,0,10),BorderFactory.createLineBorder(styleClass.DIALOG_BG)));
        label.setForeground(styleClass.DIALOG_FG);
        getContentPane().setBackground(styleClass.DIALOG_BG);
    }
    
}
