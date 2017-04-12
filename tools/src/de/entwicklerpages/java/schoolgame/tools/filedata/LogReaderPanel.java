package de.entwicklerpages.java.schoolgame.tools.filedata;

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

import de.entwicklerpages.java.schoolgame.tools.PathHelper;


public class LogReaderPanel extends JPanel implements ActionListener {

    private JTextArea textArea = null;
    private JButton reloadButton = null;

    private final File logFile;


    public LogReaderPanel(String logFileName)
    {
        super(new BorderLayout());

        String logPath = PathHelper.getBasePath();
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

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.reloadButton)
        {
            loadText();
        }
    }
}
