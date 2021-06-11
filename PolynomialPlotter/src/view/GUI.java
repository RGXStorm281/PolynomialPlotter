package view;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @author raphaelsack
 */

public class GUI extends JFrame implements IGUI {

    private float zoom;

    public GUI() {
        // Add custom icon
        super();
        URL iconURL = getClass().getResource("../data/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        // Split up in 2 Panes
        Sidebar sidebar_panel = new Sidebar();
        Plotter plotter_panel = new Plotter();
        getContentPane().add(sidebar_panel, BorderLayout.WEST);
        getContentPane().add(plotter_panel, BorderLayout.CENTER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Polynomial plotter");

    }

    public void start() {
        if (!isVisible())
            setVisible(true);
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getZoom() {
        // TODO Auto-generated method stub
        return this.zoom;
    }

}
