package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import de.entwicklerpages.java.schoolgame.game.dialog.CharacterType;

public class CharacterEditorPanel extends BasePanel implements ActionListener {

    private DialogEditor.CharacterNode characterNode = null;

    private JTextField idTextField;
    private JTextField titleTextField;
    private JCheckBox imageCheckbox;
    private JButton saveButton;
    private JButton removeButton;

    private boolean canRemove = true;

    public CharacterEditorPanel(DialogEditor editor, DialogEditor.CharacterNode characterNode)
    {
        super(new BorderLayout(), editor);

        this.characterNode = characterNode;

        canRemove = editor.getLevel().getCharacters().getCharacter().size() > 1;

        add(new JLabel("Charakter Editor: " + characterNode.getCharacter().getId()), BorderLayout.NORTH);

        add(buildEditor(characterNode.getCharacter()), BorderLayout.CENTER);
    }

    private JPanel buildEditor(CharacterType character)
    {
        JPanel editor = new JPanel(new GridBagLayout());
        editor.setBorder(new EmptyBorder(10, 30, 10, 30));

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
        gbc.insets.top = 20;
        saveButton = new JButton("Übernehmen");
        saveButton.addActionListener(this);
        editor.add(saveButton, gbc);

        gbc.gridy++;
        gbc.insets.top = 0;
        removeButton = new MetalGradientButton("Charakter entfernen");
        removeButton.addActionListener(this);
        removeButton.setBackground(Color.RED.darker());
        removeButton.setEnabled(canRemove);
        editor.add(removeButton, gbc);

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
        else if (event.getSource() == removeButton && canRemove)
        {
            int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                    "Wollen Sie wirklich diesen Charakter (" + characterNode.getCharacter().getId() + ") löschen?", "Charakter löschen", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                editor.getLevel().getCharacters().getCharacter().remove(characterNode.getCharacter());
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)characterNode.getParent();
                parent.remove(characterNode);
                editor.updateTree(parent);
            }
        }
    }

    private void saveId()
    {
        String idText = idTextField.getText();

        if (idText.isEmpty()) idText = "leer";
        if (idText.equals("#player#")) idText = "player";

        characterNode.getCharacter().setId(idText);
        updateTree(characterNode);
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
