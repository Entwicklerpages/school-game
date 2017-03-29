package de.entwicklerpages.java.schoolgame.tools.dialog;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.StatementType;
import de.entwicklerpages.java.schoolgame.game.dialog.TextsType;

public class DialogEditorPanel extends BasePanel implements ActionListener {

    private DialogEditor.DialogNode dialogNode = null;

    private JTextField nameTextField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton addStatementButton;

    public DialogEditorPanel(DialogEditor editor, DialogEditor.DialogNode dialogNode)
    {
        super(new BorderLayout(), editor);

        this.dialogNode = dialogNode;

        add(new JLabel("Dialog Editor: " + dialogNode.getDialog().getName()), BorderLayout.NORTH);

        add(buildEditor(dialogNode.getDialog()), BorderLayout.CENTER);
    }

    private JPanel buildEditor(DialogType dialog)
    {
        JPanel editor = new JPanel(new GridBagLayout());
        editor.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        editor.add(new JLabel("Name"), gbc);

        gbc.gridx++;
        nameTextField = new JTextField(dialog.getName());
        nameTextField.addActionListener(this);
        nameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isUpperCase(keyChar)) {
                    e.setKeyChar(Character.toLowerCase(keyChar));
                }
            }
        });
        editor.add(nameTextField, gbc);

        gbc.insets.top = 20;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        saveButton = new JButton("Übernehmen");
        saveButton.addActionListener(this);
        editor.add(saveButton, gbc);

        gbc.insets.top = 0;
        gbc.gridy++;
        removeButton = new MetalGradientButton("Dialog entfernen");
        removeButton.addActionListener(this);
        removeButton.setBackground(Color.RED.darker());
        editor.add(removeButton, gbc);

        gbc.gridy++;
        gbc.insets.top = 20;
        editor.add(new JLabel("Anzahl Statements: " + dialog.getStatement().size(), JLabel.CENTER), gbc);

        gbc.gridy++;
        gbc.insets.top = 0;
        addStatementButton = new MetalGradientButton("Statement hinzufügen");
        addStatementButton.addActionListener(this);
        addStatementButton.setBackground(Color.getHSBColor(0.370588f, 0.24f, 1.0f));
        editor.add(addStatementButton, gbc);

        return editor;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == nameTextField || event.getSource() == saveButton)
        {
            saveName();
        }
        else if (event.getSource() == removeButton)
        {
            int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                    "Wollen Sie wirklich diesen Dialog (" + dialogNode.getDialog().getName() + ") löschen?", "Dialog löschen", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                editor.getLevel().getDialogs().getDialog().remove(dialogNode.getDialog());
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)dialogNode.getParent();
                parent.remove(dialogNode);
                editor.updateTree(parent);
            }
        }
        else if (event.getSource() == addStatementButton)
        {
            StatementType newStatement = new StatementType();
            newStatement.setTalking("");
            DefaultMutableTreeNode statementNode = editor.new StatementNode(dialogNode.getDialog(), newStatement);
            dialogNode.add(statementNode);

            TextsType newText = new TextsType();
            newText.getText().add("");
            statementNode.add(editor.new TextNode(dialogNode.getDialog(), newStatement, "", 0));

            newStatement.setTexts(newText);
            dialogNode.getDialog().getStatement().add(newStatement);

            dialogNode.add(statementNode);
            this.editor.updateTree(dialogNode);
        }
    }

    private void saveName()
    {
        dialogNode.getDialog().setName(nameTextField.getText());
        updateTree(dialogNode);
    }
}
