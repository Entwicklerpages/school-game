package de.entwicklerpages.java.schoolgame.tools.dialog;

import javax.swing.JPanel;


public class DialogEditorPanel extends JPanel {

    private DialogEditor.DialogNode dialogNode = null;

    public DialogEditorPanel(DialogEditor.DialogNode dialogNode)
    {
        super();

        this.dialogNode = dialogNode;
    }
}
