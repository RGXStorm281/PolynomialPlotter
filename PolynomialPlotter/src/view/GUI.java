package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import model.DrawingInformationContainer;
import event.FunctionEditedEvent;
import event.FunctionEvent;
import event.IFunctionListener;
import event.IPlotListener;
import logic.FunctionParsingException;
import model.IFunction;
import model.ISettings;

/**
 * @author raphaelsack
 */
public class GUI extends JFrame implements IGUI {
    
 
    
    // Aufteilung der GUI in 3 Hauptkomponenten
    private JCustomMenuBar menubar;
    private JSidebar sidebar_panel;
    private JScrollPane scroll_sidebar_panel;
    private JPlotter plotter_panel;
    
    private final ISettings settings;
    private StyleClass styleClass;
    


    public GUI(ISettings settings) throws FileNotFoundException, IOException {
        super();
        this.settings = settings;
        this.styleClass = new StyleClass(settings.getTheme());
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
        UIManager.put("ScrollBarUI", "view.MyScrollBarUI");
        // Add custom icon
        InputStream in = getClass().getResourceAsStream("data/icon.png");
        try {
            setIconImage(new ImageIcon(in.readAllBytes()).getImage());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        pack();

        // Split up in 2 Panes
        sidebar_panel = new JSidebar(styleClass);
        plotter_panel = new JPlotter(this.settings, styleClass);
        scroll_sidebar_panel = new JScrollPane(sidebar_panel);
        scroll_sidebar_panel.getVerticalScrollBar().setUI(new JScrollBarUICustom(styleClass));
        scroll_sidebar_panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_sidebar_panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_sidebar_panel.getVerticalScrollBar().setUnitIncrement(16);
        scroll_sidebar_panel.setBorder(null);
        menubar = new JCustomMenuBar(this, styleClass);
        getContentPane().add(menubar, BorderLayout.NORTH);
        getContentPane().add(scroll_sidebar_panel, BorderLayout.WEST);
        getContentPane().add(plotter_panel, BorderLayout.CENTER);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Polynomial plotter");
        setLocationRelativeTo(null);

    }
    @Override
    public void updateTheme() {
        styleClass.update();
        menubar.recolor();
        sidebar_panel.recolor();
        plotter_panel.recolor();
    }

    protected void changeTheme(String newPath) {
        styleClass.change(newPath);
        menubar.recolor();
        sidebar_panel.recolor();
        plotter_panel.recolor();
    }

    public void simulateAddFunction(Color col, String functionString, String functionName) {
        for (IFunctionListener fl : sidebar_panel.getFunctionListeners()) {
            try {
                fl.functionAdded(new FunctionEvent(plotter_panel, col, functionString, functionName));
            } catch (FunctionParsingException e) {
                e.printStackTrace();
            }
        }
    }

    public void simulateRemoveFunction(String functionName) {
        for (IFunctionListener fl : sidebar_panel.getFunctionListeners()) {
            fl.functionDeleted(new FunctionEvent(plotter_panel, null, null, functionName));
        }
    }

    public void simulateEditFunction(Color col, String functionString, String functionName, String oldFunctionString) {
        for (IFunctionListener fl : sidebar_panel.getFunctionListeners()) {
            try {
                fl.functionEdited(
                        new FunctionEditedEvent(plotter_panel, col, functionString, functionName, oldFunctionString));
            } catch (FunctionParsingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public JPlotter getPlotter() {
        return this.plotter_panel;
    }
    @Override
    public void start() {
        if (!isVisible()) {
            setVisible(true);
        }
    }


   
    @Override
    public int getPlotWidth() {
        return plotter_panel.getWidth();
    }
    @Override    
    public int getPlotHeight() {
        return plotter_panel.getHeight();
    }

    @Override
    public void drawFunctions(DrawingInformationContainer drawingInformation) {
        plotter_panel.updateDrawingInformation(drawingInformation);
        updateTheme();
    }
    
    @Override
    public void updateFunctionList(IFunction[] functions){
        sidebar_panel.updateFunctionComponents(functions);
    }

    @Override
    public void addFunctionListener(IFunctionListener functionListener) {
        this.sidebar_panel.addFunctionListener(functionListener);

    }

    @Override
    public void addPlotListener(IPlotListener plotListener) {
        this.plotter_panel.addPlotListener(plotListener);

    }

    /**
     * Funktion um einen "dekorierten" Polynominal-Ausdruck, in einen
     * undekorierten String zu verwandeln. z.B.: 4x??+2x??+x+1 wird zu
     * 4x^3+2x^2+x+1
     *
     * @param str dekorierter String
     * @return undekorierter String
     */
    protected static String undecorate(String str) {
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
     * Funktion um einen "undekorierten" Polynominal-Ausdruck, in einen
     * dekorierten String zu verwandeln. z.B.: 4x^3+2x^2+x+1 wird zu 4x??+2x??+x+1
     *
     * @param str undekorierter String
     * @return dekorierter String
     */
    protected static String decorate(String str) {
        char[] chars = str.toCharArray();
        if (chars.length == 0) {
            return "";
        }
        boolean superscripted = false;
        String res = "";
        for (int i = 0; i < chars.length; i++) {
            int uni = (int) chars[i];
            if (uni == 94) {
                superscripted = !superscripted;
                if(chars[i+1] == 'x' || chars[i+1]=='-' || chars[i+1] == '(')return str;
                continue;
            }
            if (!superscripted) {
                res += chars[i];
                continue;
            }
            if (uni < 48 || uni > 57) {
                res += chars[i];
                superscripted = !superscripted;
                continue;
            }
            switch (chars[i]) {
                case '0':
                    res += '???';
                    break;
                case '1':
                    res += '??';
                    break;
                case '2':
                    res += '??';
                    break;
                case '3':
                    res += '??';
                    break;
                case '4':
                    res += '???';
                    break;
                case '5':
                    res += '???';
                    break;
                case '6':
                    res += '???';
                    break;
                case '7':
                    res += '???';
                    break;
                case '8':
                    res += '???';
                    break;
                case '9':
                    res += '???';
            }
        }
        if (chars[chars.length - 1] == '^') {
            res += '^';
        }
        return res;
    }
}
