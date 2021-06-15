package view;

import javax.swing.SwingUtilities;

public class GUI_Helper {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override 
            public void run(){
                GUI gui = new GUI();
                gui.start();
            }
        });
    }
}
