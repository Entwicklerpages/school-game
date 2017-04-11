package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import de.entwicklerpages.java.schoolgame.game.dialog.CharacterType;
import de.entwicklerpages.java.schoolgame.game.dialog.CharactersType;


public class CharactersOverviewPanel extends BasePanel implements ActionListener {

    private final CharactersType characters;
    private final DefaultMutableTreeNode charactersNode;

    private final JButton addCharacterButton;

    public CharactersOverviewPanel(DialogEditor editor, DefaultMutableTreeNode charactersNode, CharactersType characters)
    {
        super(new BorderLayout(), editor);

        this.charactersNode = charactersNode;
        this.characters = characters;

        add(new JLabel("Anzahl Charaktere: " + this.characters.getCharacter().size(), JLabel.CENTER), BorderLayout.NORTH);

        add(new JPanel(), BorderLayout.CENTER);

        addCharacterButton = new JButton("Neuen Charakter anlegen");
        addCharacterButton.addActionListener(this);
        add(addCharacterButton, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == addCharacterButton)
        {
            CharacterType newCharacter = new CharacterType();
            newCharacter.setId("neu");
            newCharacter.setTitle("Neuer Charakter");
            newCharacter.setImage("no");

            this.characters.getCharacter().add(newCharacter);
            charactersNode.add(editor.new CharacterNode(newCharacter));
            this.editor.updateTree(charactersNode);
        }
    }
}
