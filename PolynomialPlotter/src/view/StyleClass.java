package view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.awt.Color;

public class StyleClass {

    public Color DIALOG_BG;
    public Color DIALOG_FG;
    public Color SIDEBAR_BG_COLOR;
    public Color HEADING_BG_COLOR;
    public Color SIDEBAR_ADD_BUTTON_BG;
    public Color SIDEBAR_ADD_BUTTON_FG;
    public Color HEADING_FG_COLOR;
    public Color FUNCTION_CARD_BG;
    public Color FUNCTION_CARD_BG_HOVER;
    public Color FUNCTION_CARD_FG;
    public Color FUNCTION_CARD_CLOSE_BUTTON_BG;
    public Color FUNCTION_CARD_CLOSE_BUTTON_FG;
    public Color FUNCTION_CARD_EDIT_BUTTON_BG;
    public Color BUTTON_BG;
    public Color BUTTON_FG;
    public Color GIRD_COLOR;
    public Color PLOT_BG;
    public Color PLOT_FG;
    public Color MENU_BG;
    public Color MENU_FG;
    public Color MENU_BG_SELECTION;
    public Color MENU_FG_SELECTION;

    private String currentPath;
    public Color MENU_ACCEL;

    public StyleClass(String path) throws FileNotFoundException, IOException {
        currentPath = path;
        Properties propertiesFile = new Properties();
        propertiesFile.load(new FileReader(path));
        this.SIDEBAR_BG_COLOR = Color.decode(propertiesFile.getProperty("sidebar-background-color"));
        this.HEADING_BG_COLOR = Color.decode(propertiesFile.getProperty("heading-background-color"));
        this.SIDEBAR_ADD_BUTTON_BG = Color.decode(propertiesFile.getProperty("add-button-bg"));
        this.SIDEBAR_ADD_BUTTON_FG = Color.decode(propertiesFile.getProperty("add-button-fg"));
        this.HEADING_FG_COLOR = Color.decode(propertiesFile.getProperty("heading-color"));
        this.FUNCTION_CARD_BG = Color.decode(propertiesFile.getProperty("function-card-bg"));
        this.FUNCTION_CARD_BG_HOVER = Color.decode(propertiesFile.getProperty("function-card-bg-hover"));
        this.FUNCTION_CARD_FG = Color.decode(propertiesFile.getProperty("function-card-fg"));
        this.FUNCTION_CARD_CLOSE_BUTTON_BG = Color
                .decode(propertiesFile.getProperty("function-card-close-button-background"));
        this.FUNCTION_CARD_CLOSE_BUTTON_FG = Color
                .decode(propertiesFile.getProperty("function-card-close-button-color"));
        this.FUNCTION_CARD_EDIT_BUTTON_BG = Color
                .decode(propertiesFile.getProperty("function-card-edit-button-background"));
        this.BUTTON_BG = Color.decode(propertiesFile.getProperty("button-background"));
        this.BUTTON_FG = Color.decode(propertiesFile.getProperty("button-color"));
        this.DIALOG_BG = Color.decode(propertiesFile.getProperty("dialog-background"));
        this.DIALOG_FG = Color.decode(propertiesFile.getProperty("dialog-color"));
        this.GIRD_COLOR = Color.decode(propertiesFile.getProperty("grid-color"));
        this.PLOT_FG = Color.decode(propertiesFile.getProperty("plot-color"));
        this.PLOT_BG = Color.decode(propertiesFile.getProperty("plot-background"));
        this.MENU_BG = Color.decode(propertiesFile.getProperty("menu-background"));
        this.MENU_FG = Color.decode(propertiesFile.getProperty("menu-color"));
        this.MENU_BG_SELECTION = Color.decode(propertiesFile.getProperty("menu-background-selection"));
        this.MENU_FG_SELECTION = Color.decode(propertiesFile.getProperty("menu-color-selection"));
        this.MENU_ACCEL = Color.decode(propertiesFile.getProperty("menu-accelerator-color"));
        
    }
    
    public void change(String newPath) {
        currentPath = newPath;
        update();
    }
    
    public void update() {
        Properties propertiesFile = new Properties();
        try {
            propertiesFile.load(new FileReader(currentPath));
        } catch (IOException e) {
        }
        this.SIDEBAR_BG_COLOR = Color.decode(propertiesFile.getProperty("sidebar-background-color"));
        this.HEADING_BG_COLOR = Color.decode(propertiesFile.getProperty("heading-background-color"));
        this.SIDEBAR_ADD_BUTTON_BG = Color.decode(propertiesFile.getProperty("add-button-bg"));
        this.SIDEBAR_ADD_BUTTON_FG = Color.decode(propertiesFile.getProperty("add-button-fg"));
        this.HEADING_FG_COLOR = Color.decode(propertiesFile.getProperty("heading-color"));
        this.FUNCTION_CARD_BG = Color.decode(propertiesFile.getProperty("function-card-bg"));
        this.FUNCTION_CARD_BG_HOVER = Color.decode(propertiesFile.getProperty("function-card-bg-hover"));
        this.FUNCTION_CARD_FG = Color.decode(propertiesFile.getProperty("function-card-fg"));
        this.FUNCTION_CARD_CLOSE_BUTTON_BG = Color
        .decode(propertiesFile.getProperty("function-card-close-button-background"));
        this.FUNCTION_CARD_CLOSE_BUTTON_FG = Color
        .decode(propertiesFile.getProperty("function-card-close-button-color"));
        this.FUNCTION_CARD_EDIT_BUTTON_BG = Color
        .decode(propertiesFile.getProperty("function-card-edit-button-background"));
        this.BUTTON_BG = Color.decode(propertiesFile.getProperty("button-background"));
        this.BUTTON_FG = Color.decode(propertiesFile.getProperty("button-color"));
        this.DIALOG_BG = Color.decode(propertiesFile.getProperty("dialog-background"));
        this.DIALOG_FG = Color.decode(propertiesFile.getProperty("dialog-color"));
        this.GIRD_COLOR = Color.decode(propertiesFile.getProperty("grid-color"));
        this.PLOT_FG = Color.decode(propertiesFile.getProperty("plot-color"));
        this.PLOT_BG = Color.decode(propertiesFile.getProperty("plot-background"));
        this.MENU_FG = Color.decode(propertiesFile.getProperty("menu-color"));
        this.MENU_BG = Color.decode(propertiesFile.getProperty("menu-background"));
        this.MENU_FG_SELECTION = Color.decode(propertiesFile.getProperty("menu-color-selection"));
        this.MENU_BG_SELECTION = Color.decode(propertiesFile.getProperty("menu-background-selection"));
        this.MENU_ACCEL = Color.decode(propertiesFile.getProperty("menu-accelerator-color"));
    }
    
}
