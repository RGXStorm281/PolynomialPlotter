package view;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import event.FunctionEditedEvent;
import event.FunctionEvent;
import view.GUI.FontFamily;
import view.GUI.FontStyle;

import java.awt.Dimension;
import java.awt.Cursor;
import event.IFunctionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.FunctionParsingException;

public class FunctionDialog extends JFrame {

    enum DialogType{
        ADD,
        EDIT
    }

    private Color buttonBackground;
    private Color buttonForeground;


    private JButton okButton;
    private JButton cancelButton;

    private Character lastFunctionChar;

    private JTextField functionInput;
    private JLabel functionInputLabel;
    private JLabel functionErrorLabel;

    private JLabel colorLabel;

    private StyleClass styleClass;
    private JPanel colorPanel;
    private List<IFunctionListener> functionListeners = new ArrayList<IFunctionListener>();

    FunctionDialog(JFunctionComponent caller,DialogType dialogType, StyleClass styleClass) {
        this.styleClass = styleClass;
        buttonBackground = this.styleClass.BUTTON_BG;
        buttonForeground = this.styleClass.BUTTON_FG;
        getContentPane().setBackground(this.styleClass.DIALOG_BG);
        getContentPane().setForeground(this.styleClass.DIALOG_FG);
        InputStream in = getClass().getResourceAsStream("data/functionDialogIcon.png"); 
        try {
            setIconImage(new ImageIcon(in.readAllBytes()).getImage());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Font font = GUI.getFont(FontFamily.ROBOTO,FontStyle.REGULAR,16);

        setFont(font);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(476, 150));
        setType(Type.POPUP);
        setResizable(false);
        getContentPane().setLayout(null);
        functionInput = new JTextField();
        functionInput.setFont(font);
        functionInput.setForeground(this.styleClass.DIALOG_FG);
        functionInput.setBounds(10, 25, 345, 33);
        functionInput.setCaretColor(this.styleClass.DIALOG_FG);
        getContentPane().add(functionInput);
        functionInput.setColumns(10);
        functionInput.setBackground(this.styleClass.DIALOG_BG);
        functionInput.setBorder(BorderFactory.createLineBorder(this.styleClass.DIALOG_BG));
        functionInput.setBorder(BorderFactory.createCompoundBorder(
            functionInput.getBorder(),BorderFactory.createEmptyBorder(0,0,0,0)
        ));
        functionInput.setBorder(BorderFactory.createCompoundBorder(
            functionInput.getBorder(),BorderFactory.createMatteBorder(0, 0, 1, 0, this.styleClass.DIALOG_FG)
        ));

        functionInputLabel = new JLabel("Funktionausdruck");
        functionInputLabel.setFont(font.deriveFont(11f));
        functionInputLabel.setForeground(this.styleClass.DIALOG_FG);
        functionInputLabel.setBounds(10, 10, 132, 14);
        getContentPane().add(functionInputLabel);

        functionErrorLabel = new JLabel();
        functionErrorLabel.setFont(font.deriveFont(11f));
        functionErrorLabel.setBounds(10, 60, 345, 14);
        functionErrorLabel.setForeground(Color.RED);
        functionErrorLabel.setVisible(false);
        getContentPane().add(functionErrorLabel);

        colorLabel = new JLabel("Farbe:");
        colorLabel.setFont(font.deriveFont(15f));
        colorLabel.setForeground(this.styleClass.DIALOG_FG);
        colorLabel.setBounds(10, 71, 53, 20);
        getContentPane().add(colorLabel);

        okButton = new JButton("O.K.");
        okButton.setBounds(365, 25, 89, 33);
        okButton.setBackground(buttonBackground);
        okButton.setForeground(buttonForeground);
        okButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
        okButton.setFont(font.deriveFont(15f));
        // TODO: Function char schlauer machen
        switch(dialogType){
            case ADD:
            okButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                for(IFunctionListener listener: functionListeners){
                    try {
                        ((IFunctionListener)listener).functionAdded(new FunctionEvent(e.getSource(), colorPanel.getBackground(), functionInput.getText().trim(), functionStringToChar(functionInput.getText().trim())));
                        hideWarn();
                        closeDialog();
                        functionInput.setText("");
                        colorPanel.setBackground(randomColor());
                    } catch (FunctionParsingException ex) {
                        Logger.getLogger(FunctionDialog.class.getName()).log(Level.SEVERE, null, ex);
                        enableWarn(ex.getMessage());
                    }
                }
              
                }
            });
            break;
            case EDIT:
            okButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                for(IFunctionListener listener: functionListeners){
                    try {
                        ((IFunctionListener)listener).functionEdited(new FunctionEditedEvent(e.getSource(), colorPanel.getBackground(), functionInput.getText().trim(), functionStringToChar(functionInput.getText().trim()),lastFunctionChar));
                        hideWarn();
                        closeDialog();
                        caller.editFunction(functionInput.getText().trim(),colorPanel.getBackground());

                    } catch (FunctionParsingException ex) {
                        Logger.getLogger(FunctionDialog.class.getName()).log(Level.SEVERE, null, ex);
                        enableWarn(ex.getMessage());
                    }
                }
            }});
        }
        okButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                okButton.setBackground(buttonBackground.darker());
                okButton.setBorder(BorderFactory.createLineBorder(buttonBackground.darker()));
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                okButton.setBackground(buttonBackground);
                okButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
            }

        });
        getContentPane().add(okButton);
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(buttonBackground);
        cancelButton.setForeground(buttonForeground);
        cancelButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
        cancelButton.setFont(font.deriveFont(15f));
        cancelButton.setBounds(365, 69, 89, 33);
        cancelButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(buttonBackground.darker());
                cancelButton.setBorder(BorderFactory.createLineBorder(buttonBackground.darker()));
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(buttonBackground);
                cancelButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
            }

        });
        cancelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                cancelButton.setBackground(buttonBackground);
                dispose();
            }
        });
        getContentPane().add(cancelButton);

        colorPanel = new JPanel();
        colorPanel.setBackground(randomColor());
        colorPanel.setBounds(60, 68, 26, 26);
        colorPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                Color col = CustomColorChooser.showDialog(null, "Farbe wählen", colorPanel.getBackground());
                if(col != null) colorPanel.setBackground(col);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                colorPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                colorPanel.setCursor(Cursor.getDefaultCursor());
            }

        });
        getContentPane().add(colorPanel);
        pack();
        revalidate();
        repaint();
    }

    protected Character functionStringToChar(String string) {
        String[] arr = string.split("=");
        if(arr.length == 1){
            return null;
        }else{
            return arr[0].toCharArray()[0];
        }
    }

    protected void hideWarn() {
        functionErrorLabel.setVisible(false);
        colorLabel.setBounds(10, 71, 53, 20);
        colorPanel.setBounds(60, 68, 26, 26);
        this.setPreferredSize(new Dimension(476, 150));
        pack();
    }
    
    
    /** Zeigt eine Warnung unter dem InputFeld an
     * @param msg Fehlermeldung
     */
    protected void enableWarn(String msg) {
        functionErrorLabel.setText(msg);
        colorLabel.setBounds(10, 71+functionErrorLabel.getHeight(), 53, 20);
        colorPanel.setBounds(60, 68+functionErrorLabel.getHeight(), 26, 26);
        functionErrorLabel.setVisible(true);
        this.setPreferredSize(new Dimension(476, 150+functionErrorLabel.getHeight()));
        pack();
    }

    
    /**Gibt den Funktionsstring zurück 
     * @return String
     */
    public String getFunctionString(){
        return functionInput.getText();
    }

    
    /** gibt die Farbe des Dialogs zurück [Wird übers colorPanel gemacht, spart Speicherplatz]
     * @return Color
     */
    public Color getColor(){
        return colorPanel.getBackground();
    }
    
    /**
     * Schließt den Dialog
     */
    public void closeDialog(){
        okButton.setBackground(buttonBackground);
        cancelButton.setBackground(buttonBackground);
        dispose();
    }


    
    /** generiert eine zufällge Farbe (für den Color-Chooser)
     * @return Color
     */
    public static Color randomColor(){
        Color col =  new Color((int)(Math.random()*256d),(int)(Math.random()*256d),(int)(Math.random()*256d));
        if(col == new Color(241,241,241)){
            return randomColor();
        }else{
            return col;
        }
    }

    
    /** 
     * @param functionListener
     */
    public void addFunctionListener(IFunctionListener functionListener) {
        functionListeners.add(functionListener);
    }

    /**
     * Startet den Dialog
     */
    public void start() {
        SwingUtilities.invokeLater(new Runnable(){
            
        

            @Override
            public void run() {
                setVisible(true);
            }
            });
        
    }

    
    /** Ändert den Funktionsstring im Eingabefeld [Fürs Editieren wichtig]
     * @param functionString
     */
    public void setFunctionString(String functionString) {
        functionInput.setText(functionString);
    }

    
    /** Ändert die intiale Farbe des ColorChooser (Wird über das Color-Panel gehandelt [spart Speicherplatz])
     * @param circleColor
     */
    public void setColor(Color circleColor) {
        colorPanel.setBackground(circleColor);
    }

    
    /** Wenn der Dialog als Edit-Dialog gecallt wird, dann soll am anfang der alte Character mitgegeben werden
     * @param functionChar
     */
    public void setLastFunctionChar(Character functionChar) {
        this.lastFunctionChar = functionChar;
    }

    public void recolor() {
        buttonBackground = styleClass.BUTTON_BG;
        buttonForeground = styleClass.BUTTON_FG;
        functionInputLabel.setForeground(this.styleClass.DIALOG_FG);
        colorLabel.setForeground(this.styleClass.DIALOG_FG);
        functionInput.setForeground(this.styleClass.DIALOG_FG);
        functionInput.setCaretColor(this.styleClass.DIALOG_FG);
        okButton.setBackground(buttonBackground);
        okButton.setForeground(buttonForeground);
        cancelButton.setBackground(buttonBackground);
        cancelButton.setForeground(buttonForeground);
        okButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
        cancelButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
        functionInput.setBackground(this.styleClass.DIALOG_BG);
        functionInput.setBorder(BorderFactory.createLineBorder(this.styleClass.DIALOG_BG));
        functionInput.setBorder(BorderFactory.createCompoundBorder(
            functionInput.getBorder(),BorderFactory.createEmptyBorder(0,0,0,0)
        ));
        functionInput.setBorder(BorderFactory.createCompoundBorder(
            functionInput.getBorder(),BorderFactory.createMatteBorder(0, 0, 1, 0, this.styleClass.DIALOG_FG)
        ));
        getContentPane().setBackground(styleClass.DIALOG_BG);
        revalidate();
        repaint();
    }


}
