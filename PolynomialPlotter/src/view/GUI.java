package view;

import java.awt.BorderLayout;
import java.net.URL;
import java.awt.Font;
import java.awt.Color;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import model.DrawingInformationContainer;
import startup.Settings;
import event.IFunctionListener;
import event.IPlotListener;

/**
 * @author raphaelsack
 */

public class GUI extends JFrame implements IGUI {

    enum FontFamily {
        RUBIK, ROBOTO, INCONSOLATA, ASAP
    }

    enum FontStyle {
        THIN, ITALIC, BOLD, REGULAR, LIGHT
    }

    private JPlotter plotter_panel;
    private Sidebar sidebar_panel;

    private final Settings settings;
    private StyleClass styleClass;
    private JCustomMenuBar menubar;

    public GUI(Settings settings) throws FileNotFoundException, IOException {
        super();
        
        this.settings = settings;
        this.styleClass = new StyleClass(settings.THEME);
        UIManager.put("MenuItem.selectionBackground", styleClass.MENU_BG_SELECTION);
		UIManager.put("MenuItem.selectionForeground", styleClass.MENU_FG_SELECTION);
		UIManager.put("Menu.selectionBackground", styleClass.MENU_BG_SELECTION);
		UIManager.put("Menu.selectionForeground", styleClass.MENU_FG_SELECTION);
        UIManager.put("MenuItem.background", styleClass.MENU_BG);
		UIManager.put("MenuItem.foreground", styleClass.MENU_FG);
		UIManager.put("Menu.background", styleClass.MENU_BG);
		UIManager.put("Menu.foreground", styleClass.MENU_FG);
		UIManager.put("MenuBar.background", styleClass.MENU_BG);
		UIManager.put("MenuBar.foreground", styleClass.MENU_FG);
		UIManager.put("MenuItem.acceleratorForeground", styleClass.MENU_ACCEL);
        // Add custom icon
        InputStream in = getClass().getResourceAsStream("data/icon.png"); 
        try {
            setIconImage(new ImageIcon(in.readAllBytes()).getImage());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        pack();

        // Split up in 2 Panes
        sidebar_panel = new Sidebar(styleClass);
        plotter_panel = new JPlotter(this.settings,styleClass);
        sidebar_panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 116) {
                    updateTheme();
                }
            }
        });
        menubar = new JCustomMenuBar(this,styleClass);
        getContentPane().add(menubar, BorderLayout.NORTH);
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
        menubar.recolor();
        sidebar_panel.recolor();
        plotter_panel.recolor();
    }
    
    public void changeTheme(String newPath) {
        styleClass.change(newPath);
        menubar.recolor();
        sidebar_panel.recolor();
        plotter_panel.recolor();
    }

    public JPlotter getPlotter(){
        return this.plotter_panel;
    }

    public void start() {
        if (!isVisible())
            setVisible(true);
    }

    private static Font ttfBase = null;
    private static Font telegraficoFont = null;
    private static InputStream myStream = null;
    private static final String FONT_PATH_TELEGRAFICO = "data/Roboto/Roboto-Regular.ttf";
    
    public static Font getFont(FontFamily ft, FontStyle fs, float f) {
        
        try {
            myStream = GUI.class.getResourceAsStream(FONT_PATH_TELEGRAFICO);
            ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
            telegraficoFont = ttfBase.deriveFont(Font.PLAIN, f);               
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Font not loaded.");
        }
        return telegraficoFont;
        // try {
        //     switch (ft) {
        //         case ROBOTO:
        //             switch (fs) {
        //                 case BOLD:
        //                     return Font
        //                             .createFont(Font.TRUETYPE_FONT,
        //                                     Sidebar.class.getResourceAsStream("../data/Roboto/Roboto-Bold.ttf"))
        //                             .deriveFont(f);
        //                 case THIN:
        //                     return Font
        //                             .createFont(Font.TRUETYPE_FONT,
        //                                     Sidebar.class.getResourceAsStream("../data/Roboto/Roboto-Thin.ttf"))
        //                             .deriveFont(f);
        //                 default:
        //                     return Font
        //                             .createFont(Font.TRUETYPE_FONT,
        //                                     Sidebar.class.getResourceAsStream("../data/Roboto/Roboto-Regular.ttf"))
        //                             .deriveFont(f);
        //             }
        //         case INCONSOLATA:
        //             switch(fs){
        //                 case BOLD:
        //                 return Font
        //                 .createFont(Font.TRUETYPE_FONT,
        //                         Sidebar.class.getResourceAsStream("../data/Asap/Asap-Bold.ttf"))
        //                 .deriveFont(f);
        //                 case ITALIC:
        //                 return Font
        //                 .createFont(Font.TRUETYPE_FONT,
        //                         Sidebar.class.getResourceAsStream("../data/Asap/Asap-Italic.ttf"))
        //                 .deriveFont(f);
        //                 default:
        //                 return Font
        //                 .createFont(Font.TRUETYPE_FONT,
        //                         Sidebar.class.getResourceAsStream("../data/Asap/Asap-Regular.ttf"))
        //                 .deriveFont(f);
        //             }
        //         case ASAP:
        //         switch(fs){
        //             case BOLD:
        //             return Font
        //             .createFont(Font.TRUETYPE_FONT,
        //                     Sidebar.class.getResourceAsStream("../data/Inconsolata/Inconsolata-Bold.ttf"))
        //             .deriveFont(f);
        //             case LIGHT:
        //             return Font
        //             .createFont(Font.TRUETYPE_FONT,
        //                     Sidebar.class.getResourceAsStream("../data/Inconsolata/Inconsolata-Light.ttf"))
        //             .deriveFont(f);
        //             default:
        //             return Font
        //             .createFont(Font.TRUETYPE_FONT,
        //                     Sidebar.class.getResourceAsStream("../data/Inconsolata/Inconsolata-Regular.ttf"))
        //             .deriveFont(f);
        //             }
        //         default:
        //             switch (fs) {
        //                 case BOLD:
        //                     return Font
        //                             .createFont(Font.TRUETYPE_FONT,
        //                                     Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-Bold.ttf"))
        //                             .deriveFont(f); // Looks for The Font and returns the font with the size f
        //                 case ITALIC:
        //                     return Font
        //                             .createFont(Font.TRUETYPE_FONT,
        //                                     Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-ITALIC.ttf"))
        //                             .deriveFont(f); // Looks for The Font and returns the font with the size f
        //                 default:
        //                     return Font
        //                             .createFont(Font.TRUETYPE_FONT,
        //                                     Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-Regular.ttf"))
        //                             .deriveFont(f); // Looks for The Font and returns the font with the size f
        //             }
        //     }
        // } catch (FontFormatException | IOException e) {
        //     e.printStackTrace();
        //     return Font.getFont(Font.SANS_SERIF); // If the Font is not found, return a Sans-Serif Font
        // }

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
    public void addFunctionListener(IFunctionListener functionListener) {
        this.sidebar_panel.addFunctionListener(functionListener);

    }

    public void closeFunctionDialog() {
        this.sidebar_panel.getFunctionDialog().closeDialog();
    }

    @Override
    public void addPlotListener(IPlotListener plotListener) {
        this.plotter_panel.addPlotListener(plotListener);

    }

    public void addJFunctionComponent(char functionChar, String functionString, Color functionColor) {
        sidebar_panel.addJFunctionComponent(functionChar, functionString, functionColor);
    }

    /**
     * Funktion um einen "dekorierten" Polynominal-Ausdruck, in einen undekorierten String zu verwandeln.
     * z.B.: 4x³+2x²+x+1 wird zu 4x^3+2x^2+x+1
     * @param str dekorierter String
     * @return undekorierter String
     */
    public static String undecorate(String str) {
        char[] chars = str.toCharArray();
        boolean superscripted = false;
        String res = "";
        for (int i = 0; i < chars.length; i++) {
            int uni = (int) chars[i];
            if (uni == 178 || uni == 179) {
                if (!superscripted) {
                    superscripted = !superscripted;
                    res += '^';
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
    /**
     * Funktion um einen "undekorierten" Polynominal-Ausdruck, in einen dekorierten String zu verwandeln.
     * z.B.: 4x^3+2x^2+x+1 wird zu 4x³+2x²+x+1 
     * @param str undekorierter String
     * @return dekorierter String
     */
    public static String decorate(String str) {
        char[] chars = str.toCharArray();
        if(chars.length == 0)return "";
        boolean superscripted = false;
        String res = "";
        for (int i = 0; i < chars.length; i++) {
            int uni = (int) chars[i];
            if (uni == 94 ) {
                superscripted = !superscripted;
                continue;
            }
            if (superscripted) {
                if (uni >= 48 && uni <= 57) {
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
        if(chars[chars.length-1] == '^') res+='^';
        return res;
    }
}
