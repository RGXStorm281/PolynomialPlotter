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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

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

/**
 * @author raphaelsack
 * 
 */
public class FunctionDialog extends JFrame {
    // enum um Dialog für Add und Edit zu verwenden
    enum DialogType {
        ADD, EDIT
    }

    private Color buttonBackground;
    private Color buttonForeground;

    private JButton okButton;
    private JButton cancelButton;

    // Falls beim Editieren der Funktions-Character (f,g,h...) geändert wird, muss
    // der alte mitgegeben werden um ihn zu indicaten
    private String lastFunctionChar;

    private JTextField functionInput;
    private JLabel functionInputLabel;
    private JLabel functionErrorLabel;
    private JLabel colorLabel;

    private StyleClass styleClass;
    private JPanel colorPanel; // Color-Panel für die Funktion hält auch den Farbwert
    private List<IFunctionListener> functionListeners = new ArrayList<IFunctionListener>(); // Hält alle
                                                                                            // Funktions-Listener

    FunctionDialog(JFunctionComponent caller, DialogType dialogType, StyleClass styleClass) {
        this.styleClass = styleClass;

        // Farben anpassen
        buttonBackground = this.styleClass.BUTTON_BG;
        buttonForeground = this.styleClass.BUTTON_FG;
        getContentPane().setBackground(this.styleClass.DIALOG_BG);
        getContentPane().setForeground(this.styleClass.DIALOG_FG);

        // Favicon setzen
        InputStream in = getClass().getResourceAsStream("data/functionDialogIcon.png");
        try {
            setIconImage(new ImageIcon(in.readAllBytes()).getImage());
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        // Font setzen
        Font font = GUI.getFont(16);
        setFont(font);

        // Frame zum Dialog machen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(476, 150));
        setType(Type.POPUP);
        setResizable(false);

        // Da es ein Dialog ist, sind die dimensionen feste Werte
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
        functionInput.setBorder(BorderFactory.createCompoundBorder(functionInput.getBorder(),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        functionInput.setBorder(BorderFactory.createCompoundBorder(functionInput.getBorder(),
                BorderFactory.createMatteBorder(0, 0, 1, 0, this.styleClass.DIALOG_FG)));
        // "Fixt" einen "Bug" auf MacOS bei dem das Circumflex doppelt eingegeben wird.
        // Unten stehende Code Filtert doppelte Circumflexe aus
        ((PlainDocument) functionInput.getDocument()).setDocumentFilter(new DocumentFilter() {
            private static final String REGEX = "\\^{2}"; // Beim Copy+Paste werden alle doppelten Circumflexe ersetzt
                                                          // durch ein einzelnes
            private String last = "";

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                string = string.replaceAll(REGEX, "^");
                super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.equals(last) && last.equals("^")) { // Wenn das letzte Zeichen gleich dem momentanen ist und
                                                             // das Zeichen "^" ist, dann setze das neu eingebene
                                                             // Zeichen gleich ""
                    text = "";
                }
                last = text;
                super.replace(fb, offset, length, text, attrs);
            }
        });
        functionInputLabel = new JLabel("Funktionausdruck");
        functionInputLabel.setFont(font.deriveFont(11f));
        functionInputLabel.setForeground(this.styleClass.DIALOG_FG);
        functionInputLabel.setBounds(10, 10, 132, 14);
        getContentPane().add(functionInputLabel);


        // Error Label, wird nur angezeigt wenn ein Fehler auftritt
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
        switch (dialogType) {
            case ADD:
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (IFunctionListener listener : functionListeners) {
                            try {
                                String functionName = ((IFunctionListener) listener).functionAdded(new FunctionEvent(
                                        e.getSource(), colorPanel.getBackground(), functionInput.getText().trim(),
                                        functionStringToChar(functionInput.getText().trim())));
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
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (IFunctionListener listener : functionListeners) {
                            try {
                                ((IFunctionListener) listener).functionEdited(new FunctionEditedEvent(e.getSource(),
                                        colorPanel.getBackground(), functionInput.getText().trim(),
                                        functionStringToChar(functionInput.getText().trim()), lastFunctionChar));
                                hideWarn();
                                closeDialog();
                                caller.editFunction(functionInput.getText().trim(), colorPanel.getBackground());

                            } catch (FunctionParsingException ex) {
                                Logger.getLogger(FunctionDialog.class.getName()).log(Level.SEVERE, null, ex);
                                enableWarn(ex.getMessage());
                            }
                        }
                    }
                });
        }

        MouseAdapter buttonListener = new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).setBackground(buttonBackground.darker());
                ((JButton) e.getSource()).setBorder(BorderFactory.createLineBorder(buttonBackground.darker()));

            }
            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton) e.getSource()).setBackground(buttonBackground);
                ((JButton) e.getSource()).setBorder(BorderFactory.createLineBorder(buttonBackground));
            }
        };
        okButton.addMouseListener(buttonListener);
        getContentPane().add(okButton);
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(buttonBackground);
        cancelButton.setForeground(buttonForeground);
        cancelButton.setBorder(BorderFactory.createLineBorder(buttonBackground));
        cancelButton.setFont(font.deriveFont(15f));
        cancelButton.setBounds(365, 69, 89, 33);
        cancelButton.addMouseListener(buttonListener);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButton.setBackground(buttonBackground);
                dispose();
            }
        });
        getContentPane().add(cancelButton);

        colorPanel = new JPanel();
        colorPanel.setBackground(randomColor());
        colorPanel.setBounds(60, 68, 26, 26);
        colorPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Color col = CustomColorChooser.showDialog(null, "Farbe wählen", colorPanel.getBackground());
                if (col != null)
                    colorPanel.setBackground(col);
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
        getRootPane().setDefaultButton(okButton);
        getContentPane().add(colorPanel);
        pack();
        revalidate();
        repaint();
    }

    /**
     * Funktion nimmt einen Funktions-String (bsp.: f(x) = 3x) und gib den Funktions-Namen (in dem Fall f, zurück), falls der String keinen Funktionsnamen enthält, wird null zurückgegeben.
     * @param string
     * @return String
     */
    protected String functionStringToChar(String string) {
        String[] arr = string.split("=");
        if (arr.length == 1) { // Wenn kein "=" im String ist, gebe null zurück
            return null;
        } else {
            char toParse[] = arr[0].toCharArray();
            String name = "";
            for (char c : toParse) {
                if (c == '(') { // Wenn wir ein "(" erreicht haben, breche ab
                    break;
                }
                name += c; // Solange kein "(" erreicht wurde, füge das Zeichen dem Namen hinzu.
            }
            return name;
        }
    }

    protected void hideWarn() {
        functionErrorLabel.setVisible(false);
        colorLabel.setBounds(10, 71, 53, 20);
        colorPanel.setBounds(60, 68, 26, 26);
        this.setPreferredSize(new Dimension(476, 150));
        pack();
    }

    /**
     * Zeigt eine Warnung unter dem InputFeld an
     * 
     * @param msg Fehlermeldung
     */
    protected void enableWarn(String msg) {
        functionErrorLabel.setText(msg);
        colorLabel.setBounds(10, 71 + functionErrorLabel.getHeight(), 53, 20);
        colorPanel.setBounds(60, 68 + functionErrorLabel.getHeight(), 26, 26);
        functionErrorLabel.setVisible(true);
        this.setPreferredSize(new Dimension(476, 150 + functionErrorLabel.getHeight()));
        pack();
    }

    /**
     * Gibt den Funktionsstring zurück
     * 
     * @return String
     */
    public String getFunctionString() {
        return functionInput.getText();
    }

    /**
     * gibt die Farbe des Dialogs zurück [Wird übers colorPanel gemacht, spart
     * Speicherplatz]
     * 
     * @return Color
     */
    public Color getColor() {
        return colorPanel.getBackground();
    }

    /**
     * Schließt den Dialog
     */
    public void closeDialog() {
        okButton.setBackground(buttonBackground);
        cancelButton.setBackground(buttonBackground);
        dispose();
    }

    /**
     * generiert eine zufällge Farbe (für den Color-Chooser)
     * 
     * @return Color
     */
    private Color randomColor() {
        Color col = new Color((int) (Math.random() * 256d), (int) (Math.random() * 256d), (int) (Math.random() * 256d));
        if (col == this.styleClass.PLOT_BG) {
            return randomColor();
        } else {
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
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setVisible(true);
            }
        });

    }

    /**
     * Ändert den Funktionsstring im Eingabefeld [Fürs Editieren wichtig]
     * 
     * @param functionString
     */
    public void setFunctionString(String functionString) {
        functionInput.setText(functionString);
    }

    /**
     * Ändert die intiale Farbe des ColorChooser (Wird über das Color-Panel
     * gehandelt [spart Speicherplatz])
     * 
     * @param circleColor
     */
    public void setColor(Color circleColor) {
        colorPanel.setBackground(circleColor);
    }

    /**
     * Wenn der Dialog als Edit-Dialog gecallt wird, dann soll am anfang der alte
     * Character mitgegeben werden
     * 
     * @param functionChar
     */
    public void setLastFunctionChar(String functionChar) {
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
        functionInput.setBorder(BorderFactory.createCompoundBorder(functionInput.getBorder(),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        functionInput.setBorder(BorderFactory.createCompoundBorder(functionInput.getBorder(),
                BorderFactory.createMatteBorder(0, 0, 1, 0, this.styleClass.DIALOG_FG)));
        getContentPane().setBackground(styleClass.DIALOG_BG);
        revalidate();
        repaint();
    }

}
