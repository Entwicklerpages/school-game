package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class TextEditorPanel extends BasePanel implements ActionListener {
    private DialogEditor.TextNode textNode = null;
    private boolean canRemove = true;

    private JTextField contentTextField;
    private JButton saveButton;
    private JButton removeButton;

    public TextEditorPanel(DialogEditor editor, DialogEditor.TextNode textNode)
    {
        super(new BorderLayout(), editor);

        this.textNode = textNode;

        canRemove = textNode.getStatement().getTexts().getText().size() > 1;

        add(new JLabel("Text Editor: " + this.textNode.getDialog().getName()), BorderLayout.NORTH);

        add(buildEditor(this.textNode.getText()), BorderLayout.CENTER);
    }

    private JPanel buildEditor(String text)
    {
        JPanel editor = new JPanel(new GridBagLayout());
        editor.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        editor.add(new JLabel("Text"), gbc);

        gbc.gridy++;
        contentTextField = new JTextField(text);
        contentTextField.addActionListener(this);
        editor.add(contentTextField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets.top = 20;
        saveButton = new JButton("Übernehmen");
        saveButton.addActionListener(this);
        editor.add(saveButton, gbc);

        gbc.insets.top = 0;
        gbc.gridy++;
        removeButton = new MetalGradientButton("Text entfernen");
        removeButton.addActionListener(this);
        removeButton.setBackground(Color.RED.darker());
        removeButton.setEnabled(canRemove);
        editor.add(removeButton, gbc);

        return editor;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == contentTextField || event.getSource() == saveButton)
        {
            saveText();
        }
        else if (event.getSource() == removeButton && canRemove)
        {
            int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                    "Wollen Sie wirklich diesen Text (" + textNode.getId() + ") löschen?", "Text löschen", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) textNode.getParent();
                parent.remove(textNode);
                editor.updateTextNodes((DialogEditor.StatementNode) parent);
                editor.updateTree(parent);
            }
        }
    }

    private void saveText()
    {
        textNode.setText(contentTextField.getText());
        this.editor.updateTextNodes((DialogEditor.StatementNode) textNode.getParent());
    }
}
