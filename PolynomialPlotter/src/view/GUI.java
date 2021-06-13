package view;

import java.awt.BorderLayout;
import java.net.URL;

import java.awt.Font;
import java.awt.Color;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @author raphaelsack
 */

public class GUI extends JFrame implements IGUI{

    private Plotter plotter_panel;
    private Sidebar sidebar_panel;

    // Color palette
    public static final Color aktzent1 = new Color(226,0,26);
  


    public GUI() {
        super();
        // Add custom icon
        URL iconURL = getClass().getResource("../data/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        // Split up in 2 Panes
        sidebar_panel = new Sidebar();
        plotter_panel = new Plotter();
        getContentPane().add(sidebar_panel, BorderLayout.WEST);
        getContentPane().add(plotter_panel, BorderLayout.CENTER);

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

	@Override
	public int getPlotWidth() {
		return plotter_panel.getWidth();
	}


	@Override
	public int getPlotHeight() {
		return plotter_panel.getHeight();
	}


	@Override
	public float getPlotZoom() {
		return plotter_panel.getZoom();
	}
}
