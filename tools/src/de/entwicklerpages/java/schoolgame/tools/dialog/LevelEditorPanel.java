package de.entwicklerpages.java.schoolgame.tools.dialog;


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import de.entwicklerpages.java.schoolgame.game.dialog.Level;

public class LevelEditorPanel extends BasePanel implements ActionListener {

    private Level level;

    private JTextField atlasTextField;
    private JButton saveButton;

    public LevelEditorPanel(DialogEditor editor, Level level)
    {
        super(new BorderLayout(), editor);

        this.level = level;

        add(new JLabel("Level Editor", JLabel.CENTER), BorderLayout.NORTH);

        add(buildEditor(), BorderLayout.CENTER);
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
        editor.add(new JLabel("Textur Atlas"), gbc);

        gbc.gridx++;
        atlasTextField = new JTextField(level.getAtlas());
        atlasTextField.addActionListener(this);
        editor.add(atlasTextField, gbc);


        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets.top = 20;
        saveButton = new JButton("Übernehmen");
        saveButton.addActionListener(this);
        editor.add(saveButton, gbc);

        return editor;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == atlasTextField || event.getSource() == saveButton)
        {
            level.setAtlas(atlasTextField.getText());
        }
    }
}
