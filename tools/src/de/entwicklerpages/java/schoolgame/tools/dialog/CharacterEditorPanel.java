package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CharacterEditorPanel extends JPanel {

    private DialogEditor.CharacterNode characterNode = null;

    public CharacterEditorPanel(DialogEditor.CharacterNode characterNode)
    {
        super(new BorderLayout());

        this.characterNode = characterNode;

        add(new JLabel("Charakter Editor: " + characterNode.getCharacter().getId()), BorderLayout.NORTH);
    }

}
