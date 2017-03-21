package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.DialogsType;
import de.entwicklerpages.java.schoolgame.game.dialog.StatementType;
import de.entwicklerpages.java.schoolgame.game.dialog.TextsType;


public class DialogsPanel extends JPanel implements ActionListener {

    private DialogEditor editor;

    private DialogsType dialogs;

    private JButton addDialogButton;

    public DialogsPanel(DialogEditor editor, DialogsType dialogs)
    {
        super(new BorderLayout());

        this.editor = editor;
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

            StatementType newStatement = new StatementType();
            newStatement.setTalking("");

            TextsType newText = new TextsType();
            newText.getText().add("");

            newStatement.setTexts(newText);
            newDialog.getStatement().add(newStatement);

            this.dialogs.getDialog().add(newDialog);
            this.editor.rebuildTree();
        }
    }
}
