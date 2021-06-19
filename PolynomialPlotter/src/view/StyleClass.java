package view;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.awt.Color;

public class StyleClass {

    public final Color SIDEBAR_BG_COLOR;
    public final Color HEADING_BG_COLOR;
    public final Color SIDEBAR_ADD_BUTTON_BG;
    public final Color SIDEBAR_ADD_BUTTON_FG;
    public final Color HEADING_FG_COLOR;
    public final Color FUNCTION_CARD_BG;
    public final Color FUNCTION_CARD_BG_HOVER;
    public final Color FUNCTION_CARD_FG;
    public final Color FUNCTION_CARD_CLOSE_BUTTON_BG;
    public final Color FUNCTION_CARD_CLOSE_BUTTON_FG;
    public final Color FUNCTION_CARD_EDIT_BUTTON_BG;

    public StyleClass(String path) throws FileNotFoundException, IOException{

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
        this.FUNCTION_CARD_CLOSE_BUTTON_BG = Color.decode(propertiesFile.getProperty("function-card-close-button-background"));
        this.FUNCTION_CARD_CLOSE_BUTTON_FG = Color.decode(propertiesFile.getProperty("function-card-close-button-color"));
        this.FUNCTION_CARD_EDIT_BUTTON_BG = Color.decode(propertiesFile.getProperty("function-card-edit-button-background"));

    }
    
}
