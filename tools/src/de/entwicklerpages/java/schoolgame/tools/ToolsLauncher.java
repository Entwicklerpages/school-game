package de.entwicklerpages.java.schoolgame.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.entwicklerpages.java.schoolgame.tools.filedata.ConfigEditorPanel;
import de.entwicklerpages.java.schoolgame.tools.filedata.LogReaderPanel;


public class ToolsLauncher extends JFrame {

    private final static int FRAME_WIDTH = 500;
    private final static int FRAME_HEIGHT = 400;

    private JTabbedPane tabbedPane = null;

    public ToolsLauncher()
    {
        super("School Game Tools");

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setMaximumSize(new Dimension(FRAME_WIDTH + 200, FRAME_HEIGHT + 100));
        setMinimumSize(new Dimension(FRAME_WIDTH - 50, FRAME_HEIGHT - 100));
        randomPosition();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fillWindow();

        setVisible(true);
    }

    private void randomPosition()
    {
        Random r = new Random();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        int width = Math.round((float)d.getWidth() * 0.6f);
        int height = Math.round((float)d.getHeight() * 0.6f);

        int x = Math.round((float)d.getWidth() * 0.15f);
        int y = Math.round((float)d.getHeight() * 0.1f);

        x += r.nextInt(width - getWidth());
        y += r.nextInt(height - getHeight());
        setLocation(x, y);
    }

    private void fillWindow()
    {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addTab("Application Log", new LogReaderPanel("application.log"));
        tabbedPane.addTab("Error Log", new LogReaderPanel("error.log"));
        tabbedPane.addTab("Config Editor", new ConfigEditorPanel());
        tabbedPane.addTab("Dialog Editor", new JPanel());

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
