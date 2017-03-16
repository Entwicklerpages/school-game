package de.entwicklerpages.java.schoolgame.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class ToolsLauncher extends JFrame {

    public ToolsLauncher()
    {
        super("School Game Tools");

        setSize(300, 400);
        randomPosition();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

    public static void main(String[] args)  throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ToolsLauncher();
            }
        });
    }
}
