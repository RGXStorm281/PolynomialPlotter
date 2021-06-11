package view;

import java.awt.BorderLayout;
import java.net.URL;

import java.awt.Font;
import java.awt.Color;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @author raphaelsack
 */

public class GUI extends JFrame implements IGUI{

    private float zoom;

    // Colors
    public static final Color aktzent1 = new Color(226,0,26);
  


    public GUI() {
        super();
        // Add custom icon
        URL iconURL = getClass().getResource("../data/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        // Split up in 2 Panes
        Sidebar sidebar_panel = new Sidebar(this);
        Plotter plotter_panel = new Plotter();
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
			return Font.createFont(Font.TRUETYPE_FONT, Sidebar.class.getResourceAsStream("../data/Rubik/Rubik-Regular.ttf")).deriveFont(f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return Font.getFont(Font.SANS_SERIF);
		}
    
    }

	@Override
	public int getPlotWidth() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getPlotHeight() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public float getPlotZoom() {
		// TODO Auto-generated method stub
		return this.zoom;
	}
}
