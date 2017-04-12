package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.DialogsType;
import de.entwicklerpages.java.schoolgame.game.dialog.StatementType;
import de.entwicklerpages.java.schoolgame.game.dialog.TextsType;


public class DialogsOverviewPanel extends BasePanel implements ActionListener {

    private final DialogsType dialogs;
    private final DefaultMutableTreeNode dialogsNode;

    private final JButton addDialogButton;

    public DialogsOverviewPanel(DialogEditor editor, DefaultMutableTreeNode dialogsNode, DialogsType dialogs)
    {
        super(new BorderLayout(), editor);

        this.dialogsNode = dialogsNode;
        this.dialogs = dialogs;

        add(new JLabel("Anzahl Dialoge: " + this.dialogs.getDialog().size(), JLabel.CENTER), BorderLayout.NORTH);

        add(new JPanel(), BorderLayout.CENTER);

        addDialogButton = new JButton("Neuen Dialog anlegen");
        addDialogButton.addActionListener(this);
        add(addDialogButton, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == addDialogButton)
        {
            DialogType newDialog = new DialogType();
            newDialog.setName("neu");
            DefaultMutableTreeNode newDialogNode = editor.new DialogNode(newDialog);

            StatementType newStatement = new StatementType();
            newStatement.setTalking("");
            DefaultMutableTreeNode statementNode = editor.new StatementNode(newDialog, newStatement);
            newDialogNode.add(statementNode);

            TextsType newText = new TextsType();
            newText.getText().add("");
            statementNode.add(editor.new TextNode(newDialog, newStatement, "", 0));

            newStatement.setTexts(newText);
            newDialog.getStatement().add(newStatement);

            this.dialogs.getDialog().add(newDialog);
            dialogsNode.add(newDialogNode);
            this.editor.updateTree(dialogsNode);
        }
    }
}
