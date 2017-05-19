package de.entwicklerpages.java.schoolgame.tools.pack;

import com.badlogicgames.packr.PackrConfig;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import de.entwicklerpages.java.schoolgame.tools.PathHelper;

/**
 * @author nico
 */
public class PackPanel extends JPanel implements ActionListener
{
    private final FileFilter jarFilter = new FileNameExtensionFilter("Java JAR Archiv", "jar");
    private final FileFilter zipFilter = new FileNameExtensionFilter("JDK Zip Archiv", "zip");

    private JButton packBtn;

    private JCheckBox targetPlatformWin32;
    private JCheckBox targetPlatformWin64;
    private JCheckBox targetPlatformLinux32;
    private JCheckBox targetPlatformLinux64;
    private JCheckBox targetPlatformMac;

    private JTextField executableTextField;
    private JTextField mainClassTextField;
    private JTextField bundleTextField;
    private JTextField minimizeJreTextField;

    private JTextField inputJarTextField;
    private JButton inputJarBtn;
    private JTextField outputDirTextField;
    private JButton outputDirBtn;
    private JTextField jdkPathTextField;
    private JButton jdkPathBtn;

    private JProgressBar overallProgress;
    private JProgressBar subProgress;

    public PackPanel()
    {
        super(new BorderLayout());

        add(new JLabel("Game Packer."), BorderLayout.NORTH);


        JScrollPane editorScrollPane = new JScrollPane(buildEditor());
        editorScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        add(editorScrollPane, BorderLayout.CENTER);

        packBtn = new JButton("Spiel packen");
        packBtn.addActionListener(this);
        add(packBtn, BorderLayout.SOUTH);
    }

    private JPanel buildEditor()
    {
        JPanel editor = new JPanel(new GridBagLayout());
        editor.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;

        buildTitle(editor, gbc, "Zielplatformen");

        gbc.gridy++;
        targetPlatformWin32 = new JCheckBox("Windows (32bit)", true);
        editor.add(targetPlatformWin32, gbc);

        gbc.gridx++;
        targetPlatformWin64 = new JCheckBox("Windows (64bit)", true);
        editor.add(targetPlatformWin64, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        targetPlatformLinux32 = new JCheckBox("Linux (32bit)", true);
        editor.add(targetPlatformLinux32, gbc);

        gbc.gridx++;
        targetPlatformLinux64 = new JCheckBox("Linux (64bit)", true);
        editor.add(targetPlatformLinux64, gbc);

        gbc.gridy++;
        targetPlatformMac = new JCheckBox("macOS (64bit)", true);
        editor.add(targetPlatformMac, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        buildTitle(editor, gbc, "Einstellungen");

        gbc.gridy++;
        editor.add(new JLabel("Executable Name:"), gbc);

        gbc.gridx++;
        executableTextField = new JTextField("Legende der Kristalle");
        editor.add(executableTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("Hauptklasse:"), gbc);

        gbc.gridx++;
        mainClassTextField = new JTextField("de.entwicklerpages.java.schoolgame.desktop.DesktopLauncher");
        editor.add(mainClassTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("Bundle Identifier (macOS):"), gbc);

        gbc.gridx++;
        bundleTextField = new JTextField("de.entwicklerpages.java.schoolgame");
        editor.add(bundleTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("JRE verkleinern"), gbc);

        gbc.gridx++;
        minimizeJreTextField = new JTextField(PathHelper.getPackrDirIfFound() + File.separator + "minimize.json");
        editor.add(minimizeJreTextField, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy++;
        editor.add(new JLabel("Packr liefert die Möglichkeiten 'soft' und 'hard'."), gbc);
        gbc.gridy++;
        editor.add(new JLabel("Alternativ kann das JRE auch manuell verkleinert werden."), gbc);

        gbc.gridy++;
        buildTitle(editor, gbc, "Pfade");

        gbc.gridy++;
        editor.add(new JLabel("JAR Datei:"), gbc);

        gbc.gridx++;
        inputJarTextField = new JTextField(PathHelper.getBuildDirIfFound().getAbsolutePath() + File.separator + "school-game-1.0.jar");
        editor.add(inputJarTextField, gbc);

        gbc.gridx++;
        inputJarBtn = new JButton("Durchsuchen");
        inputJarBtn.addActionListener(this);
        editor.add(inputJarBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("Ausgabeverzeichnis:"), gbc);

        gbc.gridx++;
        outputDirTextField = new JTextField(PathHelper.getBuildDirIfFound().getAbsolutePath());
        editor.add(outputDirTextField, gbc);

        gbc.gridx++;
        outputDirBtn = new JButton("Durchsuchen");
        outputDirBtn.addActionListener(this);
        editor.add(outputDirBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("JDK Stammverzeichnis:"), gbc);

        gbc.gridx++;
        jdkPathTextField = new JTextField(PathHelper.getBuildDirIfFound().getAbsolutePath());
        editor.add(jdkPathTextField, gbc);

        gbc.gridx++;
        jdkPathBtn = new JButton("Durchsuchen");
        jdkPathBtn.addActionListener(this);
        editor.add(jdkPathBtn, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy++;
        gbc.insets.top = 20;
        overallProgress = new JProgressBar(0, 5);
        overallProgress.setValue(0);
        overallProgress.setStringPainted(true);
        overallProgress.setEnabled(false);
        editor.add(overallProgress, gbc);

        gbc.gridy++;
        gbc.insets.top = 10;
        subProgress = new JProgressBar();
        subProgress.setStringPainted(true);
        subProgress.setIndeterminate(false);
        subProgress.setEnabled(false);
        subProgress.setString("");
        editor.add(subProgress, gbc);

        return editor;
    }

    private void buildTitle(JPanel editor, GridBagConstraints gbc, String title)
    {
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets.top = 15;
        editor.add(new JLabel("<html><font size=\"5\" color=\"#333\">" + title + "</font></html>"), gbc);
        gbc.gridwidth = 1;
        gbc.insets.top = 0;
    }

    private void packGame()
    {
        int max = 0;
        max += targetPlatformWin32.isSelected() ? 1 : 0;
        max += targetPlatformWin64.isSelected() ? 1 : 0;
        max += targetPlatformLinux32.isSelected() ? 1 : 0;
        max += targetPlatformLinux64.isSelected() ? 1 : 0;
        max += targetPlatformMac.isSelected() ? 1 : 0;

        overallProgress.setEnabled(true);
        overallProgress.setValue(0);
        overallProgress.setMaximum(max);
        subProgress.setEnabled(true);
        subProgress.setIndeterminate(true);
        subProgress.setString("");

        PackUtility packUtility = new PackUtility();

        packUtility.setExecutable(executableTextField.getText());
        packUtility.setMainClass(mainClassTextField.getText());
        packUtility.setBundleIdentifier(bundleTextField.getText());
        packUtility.setMinimizeJre(minimizeJreTextField.getText());
        packUtility.setSourceJar(inputJarTextField.getText());
        packUtility.setOutDir(outputDirTextField.getText());

        int count = 0;

        if (targetPlatformWin32.isSelected())
        {
            packPlatform(packUtility, PackrConfig.Platform.Windows32);
            count++;
            overallProgress.setValue(count);
        }

        if (targetPlatformWin64.isSelected())
        {
            packPlatform(packUtility, PackrConfig.Platform.Windows64);
            count++;
            overallProgress.setValue(count);
        }

        if (targetPlatformLinux32.isSelected())
        {
            packPlatform(packUtility, PackrConfig.Platform.Linux32);
            count++;
            overallProgress.setValue(count);
        }

        if (targetPlatformLinux64.isSelected())
        {
            packPlatform(packUtility, PackrConfig.Platform.Linux64);
            count++;
            overallProgress.setValue(count);
        }

        if (targetPlatformMac.isSelected())
        {
            packPlatform(packUtility, PackrConfig.Platform.MacOS);
            count++;
            overallProgress.setValue(count);
        }

        JOptionPane.showMessageDialog(this, "Vorgang abgeschlossen.", "Fertig!", JOptionPane.INFORMATION_MESSAGE);

        overallProgress.setValue(0);
        overallProgress.setEnabled(false);

        subProgress.setIndeterminate(false);
        subProgress.setEnabled(false);
        subProgress.setString("");
    }

    private void packPlatform(PackUtility packUtility, PackrConfig.Platform platform)
    {
        String platformString = "Unkown!";

        switch (platform)
        {
            case Windows32:
                platformString = "Windows 32bit";
                break;

            case Windows64:
                platformString = "Windows 64bit";
                break;

            case Linux32:
                platformString = "Linux 32bit";
                break;

            case Linux64:
                platformString = "Linux 64bit";
                break;

            case MacOS:
                platformString = "macOS";
                break;
        }

        File jdkPath = askForJDK(platformString);
        if (jdkPath != null)
        {
            packUtility.setJDK(jdkPath.getAbsolutePath());
            try
            {
                packUtility.setPlatform(platform);
                packUtility.pack();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Bei der Erstellung trat ein Fehler auf.\n" + e.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sie haben keinen Pfad angegeben.\nZielplatform wird übersprungen.", "Kein Pfad angegeben!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private File askForJDK(String platform)
    {
        subProgress.setString(platform);
        JOptionPane.showMessageDialog(this, "Bitte geben Sie den Pfad zu einem JDK (zip) an.\nZielplatform: " + platform, "Packen: " + platform, JOptionPane.INFORMATION_MESSAGE);
        JFileChooser chooser = new JFileChooser(jdkPathTextField.getText());
        chooser.addChoosableFileFilter(zipFilter);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setMultiSelectionEnabled(false);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            return chooser.getSelectedFile();
        }

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == packBtn)
        {
            int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                    "Dieser Vorgang kann je nach Konfiguration eine Weile dauern.\n" +
                    "Sind Sie sicher, dass Sie den Vorgang starten wollen?", "Spiel packen", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                packGame();
            }
        }
        else if (e.getSource() == inputJarBtn)
        {
            JFileChooser chooser = new JFileChooser(inputJarTextField.getText());
            chooser.addChoosableFileFilter(jarFilter);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setMultiSelectionEnabled(false);

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();
                inputJarTextField.setText(selectedFile.getAbsolutePath());
            }
        }
        else if (e.getSource() == outputDirBtn)
        {
            JFileChooser chooser = new JFileChooser(outputDirTextField.getText());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setMultiSelectionEnabled(false);

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();
                outputDirTextField.setText(selectedFile.getAbsolutePath());
            }
        }
        else if (e.getSource() == jdkPathBtn)
        {
            JFileChooser chooser = new JFileChooser(jdkPathTextField.getText());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setMultiSelectionEnabled(false);

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();
                jdkPathTextField.setText(selectedFile.getAbsolutePath());
            }
        }
    }
}
