package view;

import java.awt.BorderLayout;
import java.net.URL;
import java.awt.Font;
import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import event.FunctionListener;
import event.PlotListener;
import model.DrawingInformationContainer;
import startup.Settings;

/**
 * @author raphaelsack
 */

public class GUI extends JFrame implements IGUI {

    enum FontFamily {
        RUBIK, ROBOTO,
    }

    enum FontStyle {
        THIN, ITALIC, BOLD, REGULAR
    }

    private JPlotter plotter_panel;
    private Sidebar sidebar_panel;

    private final Settings settings;
    private StyleClass styleClass;

    public GUI(Settings settings) throws FileNotFoundException, IOException {
        super();
        this.settings = settings;
        this.styleClass = new StyleClass("src/view/dcdark.properties");
        // Add custom icon
        URL iconURL = getClass().getResource("../data/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        pack();

        // Split up in 2 Panes
        sidebar_panel = new Sidebar(styleClass);
        plotter_panel = new JPlotter(this.settings);
        sidebar_panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 116) {
                    updateTheme();
                }
            }
        });
        getContentPane().add(sidebar_panel, BorderLayout.WEST);
        getContentPane().add(plotter_panel, BorderLayout.CENTER);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Polynomial plotter");
        setLocationRelativeTo(null);

    }

    public void updateTheme() {
        styleClass.update();
        sidebar_panel.recolor();
    }

    public void changeTheme(String newPath) {
        try {
            this.styleClass = new StyleClass(newPath);
            sidebar_panel.recolor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (!isVisible())
            setVisible(true);
    }

    public static Font getFont(FontFamily ft, FontStyle fs, float f) {
        try {
            switch (ft) {
                case ROBOTO:
                    switch (fs) {
                        case BOLD:
                            return Font
                                    .createFont(Font.TRUETYPE_FONT,
                                            Sidebar.class.getResourceAsStream("../data/Roboto/Roboto-Bold.ttf"))
                                    .deriveFont(f);
                        case THIN:
                            return Font
                                    .createFont(Font.TRUETYPE_FONT,
                                            Sidebar.class.getResourceAsStream("../data/Roboto/Roboto-Thin.ttf"))
                                    .deriveFont(f);
                        default:
                            return Font
                                    .createFont(Font.TRUETYPE_FONT,
                                            Sidebar.class.getResourceAsStream("../data/Roboto/Roboto-Regular.ttf"))
                                    .deriveFont(f);
                    }
                default:
                    switch (fs) {
                        case BOLD:
                            return Font
                                    .createFont(Font.TRUETYPE_FONT,
                                            Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-Bold.ttf"))
                                    .deriveFont(f); // Looks for The Font and returns the font with the size f
                        case ITALIC:
                            return Font
                                    .createFont(Font.TRUETYPE_FONT,
                                            Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-ITALIC.ttf"))
                                    .deriveFont(f); // Looks for The Font and returns the font with the size f
                        default:
                            return Font
                                    .createFont(Font.TRUETYPE_FONT,
                                            Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-Regular.ttf"))
                                    .deriveFont(f); // Looks for The Font and returns the font with the size f
                    }
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF); // If the Font is not found, return a Sans-Serif Font
        }

    }

    public int getPlotWidth() {
        return plotter_panel.getWidth();
    }

    public int getPlotHeight() {
        return plotter_panel.getHeight();
    }

    public double getPlotZoom() {
        return plotter_panel.getZoom();
    }

    @Override
    public void drawFunctions(DrawingInformationContainer drawingInformation) {
        // TODO RS von RE: Wäre wichtig das hier rüber zu zeichnen, damit ich testen
        // kann.
        plotter_panel.updateDrawingInformation(drawingInformation);
    }

    @Override
    public String getFunction() {
        return sidebar_panel.getFunction();
    }

    @Override
    public Color getColor() {
        return sidebar_panel.getFunctionDialog().getColor();
    }

    @Override
    public void addFunctionListener(FunctionListener functionListener) {
        this.sidebar_panel.addFunctionListener(functionListener);

    }

    public void closeFunctionDialog() {
        this.sidebar_panel.getFunctionDialog().closeDialog();
    }

    @Override
    public void addPlotListener(PlotListener plotListener) {
        this.plotter_panel.addPlotListener(plotListener);

    }

    public void addJFunctionComponent(char functionChar, String functionString, Color functionColor) {
        sidebar_panel.addJFunctionComponent(functionChar, functionString, functionColor);
    }

    public static String undecorate(String str) {
        char[] chars = str.toCharArray();
        boolean superscripted = false;
        String res = "";
        for (int i = 0; i < chars.length; i++) {
            System.out.println(chars[i] + " : " + (int) chars[i]);
            int uni = (int) chars[i];
            if (uni == 178 || uni == 179) {
                if (!superscripted) {
                    superscripted = !superscripted;
                    res += '^';
                    System.out.println((int) '^');
                }
                res += (uni - 176);
            } else if (uni >= 8308 && uni <= 8313) {
                if (!superscripted) {
                    superscripted = !superscripted;
                    res += '^';
                }
                res += (uni - 8304);
            } else if (uni == 8304) {
                if (!superscripted) {
                    superscripted = !superscripted;
                    res += '^';
                }
                res += 0;
            } else if (uni == 185) {
                if (!superscripted) {
                    superscripted = !superscripted;
                    res += '^';
                }
                res += 1;
            } else {
                superscripted = false;
                res += chars[i];
            }

        }

        return res;
    }

    public static String decorate(String str) {
        char[] chars = str.toCharArray();
        boolean superscripted = false;
        String res = "";
        for (int i = 0; i < chars.length; i++) {
            int uni = (int) chars[i];
            if (uni == 94) {
                superscripted = !superscripted;
                continue;
            }
            if (superscripted) {
                if (uni >= 48 && uni <= 57) {
                    System.out.println(chars[i] + ":" + uni + " - super: " + superscripted);
                    switch (chars[i]) {
                        case '0':
                            res += '⁰';
                            break;
                        case '1':
                            res += '¹';
                            break;
                        case '2':
                            res += '²';
                            break;
                        case '3':
                            res += '³';
                            break;
                        case '4':
                            res += '⁴';
                            break;
                        case '5':
                            res += '⁵';
                            break;
                        case '6':
                            res += '⁶';
                            break;
                        case '7':
                            res += '⁷';
                            break;
                        case '8':
                            res += '⁸';
                            break;
                        case '9':
                            res += '⁹';
                    }
                } else {
                    res += chars[i];
                    superscripted = !superscripted;
                }

            } else {
                res += chars[i];
            }
        }
        return res;
    }
}
