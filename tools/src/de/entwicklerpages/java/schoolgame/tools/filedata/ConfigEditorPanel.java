package de.entwicklerpages.java.schoolgame.tools.filedata;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.entwicklerpages.java.schoolgame.tools.PathHelper;

public class ConfigEditorPanel extends JPanel implements ActionListener {
    private JTextArea textArea = null;
    private JButton reloadButton = null;
    private JButton saveButton = null;

    private File configFile;


    public ConfigEditorPanel()
    {
        super(new BorderLayout());

        String logPath = PathHelper.getBasePath();
        configFile = new File(logPath + "de.entwicklerpages.java.schoolgame");

        textArea = new JTextArea("Keine Konfiguration geladen!");
        textArea.setEditable(true);

        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        reloadButton = new JButton("Laden");
        reloadButton.addActionListener(this);

        saveButton = new JButton("Speichern");
        saveButton.addActionListener(this);
        saveButton.setEnabled(false);

        JPanel buttonBar = new JPanel(new GridLayout(1, 2));
        buttonBar.add(reloadButton);
        buttonBar.add(saveButton);

        add(buttonBar, BorderLayout.SOUTH);
    }

    private void loadText()
    {
        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead())
        {
            textArea.setText("Die Konfiguration kann nicht geladen werden!\nMöglichweise existiert keine.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            textArea.read(reader, null);

        } catch (Exception e) {
            textArea.setText("Die Konfiguration kann nicht geladen werden:\n" + e.getMessage());
        }
    }

    private void saveText()
    {
        if (!configFile.isFile() || !configFile.canWrite())
        {
            JOptionPane.showMessageDialog(this, "Beim Speichern ist ein Fehler aufgetreten!\nDie Datei kann nicht beschrieben werden.\nEs wurde nichts verändert.", "Fehler!", JOptionPane.OK_OPTION);
            return;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));

            textArea.write(writer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Beim Speichern ist ein Fehler aufgetreten!\n" + e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.reloadButton)
        {
            loadText();
            saveButton.setEnabled(true);
        }
        else if (event.getSource() == this.saveButton)
        {
            int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                    "Bei dieser Aktion wird die Speicherstanddatei ungeprüft verändert!\n" +
                    "Möglicherweise funktioniert das Spiel dannach nicht mehr.\n" +
                    "Wollen Sie die Daten wirklich überschreiben?", "Konfiguration speichern", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                saveText();
            }
        }
    }
}
