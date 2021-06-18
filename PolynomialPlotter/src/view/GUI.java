package view;

import java.awt.BorderLayout;
import java.net.URL;
import java.awt.Font;
import java.awt.Color;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import event.FunctionListener;
import event.PlotListener;
import model.DrawingInformationContainer;
import model.UniversalFunction;
import startup.Settings;



/**
 * @author raphaelsack
 */

public class GUI extends JFrame implements IGUI{

    private JPlotter plotter_panel;
    private Sidebar sidebar_panel;

    private final Settings settings; 

    // Color palette
    public static final Color aktzent1 = new Color(226,0,26);
  


    public GUI(Settings settings) {
        super();
        this.settings = settings;
        // Add custom icon
        URL iconURL = getClass().getResource("../data/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        pack();
        
        // Split up in 2 Panes
        sidebar_panel = new Sidebar();
        plotter_panel = new JPlotter(this.settings);
        getContentPane().add(sidebar_panel, BorderLayout.WEST);
        getContentPane().add(plotter_panel, BorderLayout.CENTER);
        plotter_panel.requestFocus();
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Polynomial plotter");
        

    }


    public void start() {
        if (!isVisible())
            setVisible(true);
    }

    public static Font getFont(float f){
        try {
			return Font.createFont(Font.TRUETYPE_FONT, Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-Regular.ttf")).deriveFont(f); // Looks for The Font and returns the font with the size f
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
        // TODO RS von RE: Wäre wichtig das hier rüber zu zeichnen, damit ich testen kann.
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


  
}
