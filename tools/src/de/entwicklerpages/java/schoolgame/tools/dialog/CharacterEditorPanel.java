package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.entwicklerpages.java.schoolgame.game.dialog.CharacterType;

public class CharacterEditorPanel extends JPanel implements ActionListener {

    private DialogEditor.CharacterNode characterNode = null;

    private JTextField idTextField;
    private JTextField titleTextField;
    private JCheckBox imageCheckbox;
    private JButton saveButton;

    public CharacterEditorPanel(DialogEditor.CharacterNode characterNode)
    {
        super(new BorderLayout());

        this.characterNode = characterNode;

        add(new JLabel("Charakter Editor: " + characterNode.getCharacter().getId()), BorderLayout.NORTH);

        add(buildEditor(characterNode.getCharacter()), BorderLayout.CENTER);
    }

    private JPanel buildEditor(CharacterType character)
    {
        JPanel editor = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        editor.add(new JLabel("ID"), gbc);

        gbc.gridx++;
        idTextField = new JTextField(character.getId());
        idTextField.addActionListener(this);
        idTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isUpperCase(keyChar)) {
                    e.setKeyChar(Character.toLowerCase(keyChar));
                }
            }
        });
        editor.add(idTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("Titel"), gbc);

        gbc.gridx++;
        titleTextField = new JTextField(character.getTitle());
        titleTextField.addActionListener(this);
        editor.add(titleTextField, gbc);

        gbc.gridy++;
        imageCheckbox = new JCheckBox("Bild anzeigen", character.getImage().equals("yes"));
        imageCheckbox.addActionListener(this);
        editor.add(imageCheckbox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        saveButton = new JButton("Übernehmen");
        saveButton.addActionListener(this);
        editor.add(saveButton, gbc);

        return editor;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == idTextField)
        {
            saveId();
        }
        else if (event.getSource() == titleTextField)
        {
            saveTitle();
        }
        else if (event.getSource() == imageCheckbox)
        {
            saveImage();
        }
        else if (event.getSource() == saveButton)
        {
            saveId();
            saveTitle();
            saveImage();
        }
    }

    private void saveId()
    {
        characterNode.getCharacter().setId(idTextField.getText());
    }

    private void saveTitle()
    {
        characterNode.getCharacter().setTitle(titleTextField.getText());
    }

    private void saveImage()
    {
        characterNode.getCharacter().setImage(imageCheckbox.isSelected() ? "yes" : "no");
    }
}
