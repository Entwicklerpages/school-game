package de.entwicklerpages.java.schoolgame.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.entwicklerpages.java.schoolgame.tools.dialog.DialogEditor;
import de.entwicklerpages.java.schoolgame.tools.filedata.ConfigEditorPanel;
import de.entwicklerpages.java.schoolgame.tools.filedata.LogReaderPanel;
import de.entwicklerpages.java.schoolgame.tools.map.MapPanel;


public class ToolsLauncher extends JFrame {

    private final static int FRAME_WIDTH = 470;
    private final static int FRAME_HEIGHT = 320;

    private Dimension screenSize = null;

    public ToolsLauncher()
    {
        super("School Game Tools");

        int startWidth = Math.round((float)getScreenSize().getWidth() * 0.4f);
        int startHeight = Math.round((float)getScreenSize().getHeight() * 0.4f);

        setSize(startWidth, startHeight);
        setMaximumSize(new Dimension(getScreenSize().width - 50, getScreenSize().height - 80));
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        randomPosition();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fillWindow();

        setVisible(true);
    }

    private Dimension getScreenSize()
    {
        if (screenSize == null)
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }

    private void randomPosition()
    {
        Random r = new Random();
        Dimension d = getScreenSize();

        int width = Math.round((float)d.getWidth() * 0.65f) - getWidth();
        int height = Math.round((float)d.getHeight() * 0.65f) - getHeight();

        int x = Math.round((float)d.getWidth() * 0.15f);
        int y = Math.round((float)d.getHeight() * 0.1f);

        x += width < 5 ? 0 : r.nextInt(width);
        y += height < 5 ? 0 : r.nextInt(height);
        setLocation(x, y);
    }

    private void fillWindow()
    {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addTab("Application Log", new LogReaderPanel("application.log"));
        tabbedPane.addTab("Error Log", new LogReaderPanel("error.log"));
        tabbedPane.addTab("Config Editor", new ConfigEditorPanel());
        tabbedPane.addTab("Dialog Editor", new DialogEditor());
        tabbedPane.addTab("Tiled Map", new MapPanel());
        tabbedPane.addTab("Über", new AboutPanel());

        add(tabbedPane);
    }

    public static void main(String[] args)  throws Exception {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ToolsLauncher();
            }
        });
    }
}
