package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.awt.Font;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JMenu;

import java.awt.Graphics2D;
import java.awt.FileDialog;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

/**
 * @author raphaelsack
 */

public class JCustomMenuBar extends JMenuBar {
    private GUI gui;
    private StyleClass styleClass;
    private JThemeDialog themeDialog;

    public JCustomMenuBar(GUI _gui, StyleClass styleClass) {
        this.gui = _gui;
        themeDialog = new JThemeDialog(gui, styleClass);
        ResourceBundle rb = ResourceBundle.getBundle("i18n.resource", getDefaultLocale()); // Resource Bundle for
                                                                                           // localization
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.styleClass = styleClass;

        // Datei
        JMenu menu_data = new JMenu(rb.getString("file"));
        menu_data.setMnemonic('D');
        JMenuItem makeScreenshot = new JMenuItem(rb.getString("saveAsPNG"));
        makeScreenshot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        makeScreenshot.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(gui, rb.getString("saveAs"), FileDialog.SAVE);
                fd.setDirectory("%USERPROFILE%\\Pictures");
                String fileName = new SimpleDateFormat("'graph_'yyyyMMddHHmm'.png'").format(new Date());
                fd.setFile(fileName);
                fd.setVisible(true);
                String filename = fd.getDirectory() + fd.getFile();
                if (filename != null) {
                    Container pane = gui.getPlotter();
                    BufferedImage img = new BufferedImage(gui.getPlotWidth(), gui.getPlotHeight(),BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = img.createGraphics();
                    pane.printAll(g2d);
                    g2d.dispose();
                    try {
                        ImageIO.write(img, "png", new File(filename));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        menu_data.add(makeScreenshot);
        menu_data.add(new JSeparator());
        JMenuItem exit_item = new JMenuItem(rb.getString("close"));
        exit_item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.dispatchEvent(new WindowEvent(gui, WindowEvent.WINDOW_CLOSING));
            };
        });
        menu_data.add(exit_item);
        add(menu_data);

        // Bearbeiten
        JMenu menu_edit = new JMenu(rb.getString("edit"));
        menu_edit.setMnemonic('E');
        JMenuItem item_theme = new JMenuItem(rb.getString("design"));
        item_theme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themeDialog.setVisible(true);
            }
        });
        JMenuItem item_refresh = new JMenuItem(rb.getString("refresh"));
        item_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.updateTheme();
            }
        });
        item_refresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        menu_edit.add(item_refresh);
        menu_edit.add(item_theme);
        add(menu_edit);

        // Hilfe
        JMenu menu_help = new JMenu(rb.getString("help"));
        menu_help.setMnemonic('h');
        JMenuItem show_help_item = new JMenuItem(rb.getString("showHelp"));
        show_help_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        show_help_item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                URI uri;
                try {
                    uri = new URI("https://www2.ba-horb.de/~i20029/programmieren/polynomialplotter/help.html");
                    java.awt.Desktop.getDesktop().browse(uri);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            };
        });
        menu_help.add(show_help_item);
        menu_help.add(new JSeparator());

        JMenuItem info_item = new JMenuItem(rb.getString("info"));
        info_item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                URI uri;
                try {
                    uri = new URI("https://www2.ba-horb.de/~i20029/programmieren/polynomialplotter/about.html");
                    java.awt.Desktop.getDesktop().browse(uri);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            };
        });
        menu_help.add(info_item);
        add(menu_help);
        recolor();
    }

    public void recolor() {
        themeDialog.recolor();
        Font font = styleClass.getFont(15);
        for (Component c : getComponents()) {
            if (!(c instanceof JMenu)) {
                continue;
            }
            JMenu m = (JMenu) c;
            m.getPopupMenu().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            m.getPopupMenu().setBackground(styleClass.MENU_BG);
            m.setBorder(null);
            m.setFont(font);
            for (Component mc : m.getMenuComponents()) {
                if (!(mc instanceof JMenuItem)) {
                    continue;
                }
                JMenuItem mi = (JMenuItem) mc;
                mi.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
                mi.setFont(font.deriveFont(13));
            }
        }

    }

}
