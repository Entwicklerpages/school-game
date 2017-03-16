package de.entwicklerpages.java.schoolgame.tools.log;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class LogReaderPanel extends JPanel implements ActionListener {

    private JTextArea textArea = null;
    private JButton reloadButton = null;

    private File logFile;


    public LogReaderPanel(String logFileName)
    {
        super(new BorderLayout());

        String logPath = getBasePath("schoolgame");
        logFile = new File(logPath + logFileName);

        textArea = new JTextArea("Keine Logs geladen!");
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        reloadButton = new JButton("Laden");
        reloadButton.addActionListener(this);

        add(reloadButton, BorderLayout.SOUTH);
    }

    private void loadText()
    {
        if (!logFile.exists() || !logFile.isFile() || !logFile.canRead())
        {
            textArea.setText("Die Logdatei kann nicht geladen werden!\nMöglichweise existiert keine.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFile));

            textArea.read(reader, null);

        } catch (Exception e) {
            textArea.setText("Die Logdatei kann nicht geladen werden:\n" + e.getMessage());
        }
    }

    private String getBasePath(String game)
    {
        String os = System.getProperty("os.name").toLowerCase();

        String base = ".prefs/" + game + "/";

        boolean absolutePath = false;

        if (os.contains("win"))
        {
            String appdata = System.getenv("APPDATA");

            if (appdata != null)
            {
                base = appdata + File.separator + game + File.separator;
                absolutePath = true;
            }
        }
        else if (os.contains("mac"))
        {
            base = "Library/Application Support/" + game + "/";
        }

        if (!absolutePath)
        {
            base = System.getProperty("user.home") + File.separator + base;
        }

        return base;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.reloadButton)
        {
            loadText();
        }
    }
}
