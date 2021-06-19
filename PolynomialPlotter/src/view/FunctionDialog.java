package view;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.JTextField;

import event.FunctionEditedEvent;
import event.FunctionEvent;
import event.FunctionListener;
import view.GUI.FontFamily;
import view.GUI.FontStyle;

import java.awt.Dimension;
import java.awt.Cursor;

public class FunctionDialog extends JFrame {

    enum DialogType{
        ADD,
        EDIT
    }

    private JButton okButton;
    private JButton cancelButton;

    private char lastFunctionChar;

    private JTextField functionInput;
    private JLabel functionInputLabel;
    private JLabel functionErrorLabel;

    private JLabel colorLabel;

    private JPanel colorPanel;
    private List<FunctionListener> functionListeners = new ArrayList<FunctionListener>();

    FunctionDialog(DialogType dialogType) {
        URL iconURL = getClass().getResource("../data/functionDialogIcon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        Font font = GUI.getFont(FontFamily.ROBOTO,FontStyle.REGULAR,16);
        getContentPane().setBackground(new Color(241,241,241));
        setFont(font);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(476, 150));
        setType(Type.POPUP);
        setResizable(false);
        getContentPane().setLayout(null);
        functionInput = new JTextField();
        functionInput.setFont(font);
        functionInput.setBounds(10, 25, 345, 33);
        getContentPane().add(functionInput);
        functionInput.setColumns(10);
        functionInput.setBorder(BorderFactory.createLineBorder(Color.decode("0xFFFFFF")));
        functionInput.setBorder(BorderFactory.createCompoundBorder(
        functionInput.getBorder(), 
        BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        functionInputLabel = new JLabel("Funktionausdruck");
        functionInputLabel.setFont(font.deriveFont(11f));
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
        colorLabel.setBounds(10, 71, 53, 20);
        getContentPane().add(colorLabel);

        okButton = new JButton("O.K.");
        okButton.setBounds(365, 25, 89, 33);
        okButton.setBackground(Color.decode("0xDEDEDE"));
        okButton.setBorder(BorderFactory.createLineBorder(Color.decode("0xDEDEDE")));
        okButton.setFont(font.deriveFont(15f));

        switch(dialogType){
            case ADD:
            okButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                boolean legalFunction = false;
                for(FunctionListener listener: functionListeners)legalFunction = ((FunctionListener)listener).functionAdded(new FunctionEvent(e.getSource(), colorPanel.getBackground(), functionInput.getText().trim(), functionInput.getText().trim().toCharArray()[0]));
                if(legalFunction){
                    //Add the function
                    hideWarn();
                    closeDialog();
                }else{
                    enableWarn("Eingegebene Funktion ist nicht valide");
                }
                }
            });
            break;
            case EDIT:
            okButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                boolean legalFunction = false;
                for(FunctionListener listener: functionListeners)legalFunction = ((FunctionListener)listener).functionEdited(new FunctionEditedEvent(e.getSource(), colorPanel.getBackground(), functionInput.getText().trim(), functionInput.getText().trim().toCharArray()[0],lastFunctionChar));
                if(legalFunction){
                    //Add the function
                    hideWarn();
                    closeDialog();
                }else{
                    enableWarn("Eingegebene Funktion ist nicht valide");
                }
                }
            });
        }
        okButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                okButton.setBackground(Color.decode("0xABABAB"));
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                okButton.setBackground(Color.decode("0xDEDEDE"));
            }

        });
        getContentPane().add(okButton);
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(365, 25, 89, 33);
        cancelButton.setBackground(Color.decode("0xDEDEDE"));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.decode("0xDEDEDE")));
        cancelButton.setFont(font.deriveFont(15f));
        cancelButton.setBounds(365, 69, 89, 33);
        cancelButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(Color.decode("0xABABAB"));
                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(Color.decode("0xDEDEDE"));
            }

        });
        cancelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                cancelButton.setBackground(Color.decode("0xDEDEDE"));
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
    public void addFunctionListener(FunctionListener functionListener) {
        functionListeners.add(functionListener);
    }

    /**
     * Startet den Dialog
     */
    public void start() {
        setVisible(true);
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
    public void setLastFunctionChar(char functionChar) {
        this.lastFunctionChar = functionChar;
    }


}
