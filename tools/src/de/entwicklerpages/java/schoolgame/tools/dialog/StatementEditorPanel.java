package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultMutableTreeNode;

import de.entwicklerpages.java.schoolgame.game.dialog.*;

public class StatementEditorPanel extends BasePanel implements ActionListener
{

    private DialogEditor.StatementNode statementNode = null;
    private boolean canRemove = false;
    private DisplayModeEnum displayMode = DisplayModeEnum.NOTHING;

    private JTextField soundTextField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton addTextButton;

    private JRadioButton displayModeNothing;
    private JRadioButton displayModePlayerOnly;
    private JRadioButton displayModePlayerAndCharacter;
    private JRadioButton displayModeCharacter;
    private JRadioButton displayModeTwoCharacters;

    private JLabel displayPositionLeft;
    private JLabel displayPositionRight;
    private JButton displayPositionSwap;

    private JComboBox displayTalking;
    private JComboBox displayCharacter1;
    private JComboBox displayCharacter2;

    public StatementEditorPanel(DialogEditor editor, DialogEditor.StatementNode statementNode)
    {
        super(new BorderLayout(), editor);

        this.statementNode = statementNode;

        canRemove = statementNode.getDialog().getStatement().size() > 1;

        add(new JLabel("Statement Editor: " + this.statementNode.getDialog().getName()), BorderLayout.NORTH);

        JScrollPane editorScrollPane = new JScrollPane(buildEditor(this.statementNode.getStatement()));
        editorScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        add(editorScrollPane, BorderLayout.CENTER);
    }

    private JPanel buildEditor(StatementType statement)
    {
        displayMode = getDisplayMode(statement);

        JPanel editor = new JPanel(new GridBagLayout());
        editor.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ////////////////////////////////////////////////////////////////////////////////////////////

        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        buildTitle(editor, gbc, "Anzeigemodus");

        ButtonGroup modeGroup = new ButtonGroup();

        gbc.gridy++;
        displayModeNothing = new JRadioButton("Keine Anzeige", displayMode == DisplayModeEnum.NOTHING);
        displayModeNothing.addActionListener(this);
        modeGroup.add(displayModeNothing);
        editor.add(displayModeNothing, gbc);

        gbc.gridx++;
        displayModePlayerOnly = new JRadioButton("Nur Spieler", displayMode == DisplayModeEnum.PLAYER_ONLY);
        displayModePlayerOnly.addActionListener(this);
        modeGroup.add(displayModePlayerOnly);
        editor.add(displayModePlayerOnly, gbc);

        gbc.gridx++;
        displayModePlayerAndCharacter = new JRadioButton("Spieler und Charakter", displayMode == DisplayModeEnum.PLAYER_AND_CHARACTER);
        displayModePlayerAndCharacter.addActionListener(this);
        modeGroup.add(displayModePlayerAndCharacter);
        editor.add(displayModePlayerAndCharacter, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        displayModeCharacter = new JRadioButton("Ein Charakter", displayMode == DisplayModeEnum.CHARACTER);
        displayModeCharacter.addActionListener(this);
        modeGroup.add(displayModeCharacter);
        editor.add(displayModeCharacter, gbc);

        gbc.gridx++;
        displayModeTwoCharacters = new JRadioButton("Zwei Charaktere", displayMode == DisplayModeEnum.TWO_CHARACTERS);
        displayModeTwoCharacters.addActionListener(this);
        modeGroup.add(displayModeTwoCharacters);
        editor.add(displayModeTwoCharacters, gbc);

        ////////////////////////////////////////////////////////////////////////////////////////////

        gbc.gridy++;
        buildTitle(editor, gbc, "Positionierung & Sprecher");

        gbc.gridy++;
        displayPositionLeft = new JLabel("Links: X");
        displayPositionLeft.setBackground(Color.WHITE);
        displayPositionLeft.setOpaque(true);
        editor.add(displayPositionLeft, gbc);

        gbc.gridx++;
        gbc.insets.left = 30;
        gbc.insets.right = 30;
        displayPositionSwap = new JButton("<->");
        displayPositionSwap.addActionListener(this);
        editor.add(displayPositionSwap, gbc);

        gbc.gridx++;
        gbc.insets.left = 0;
        gbc.insets.right = 0;
        displayPositionRight = new JLabel("Rechts: Y", JLabel.RIGHT);
        displayPositionRight.setBackground(Color.WHITE);
        displayPositionRight.setOpaque(true);
        editor.add(displayPositionRight, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets.top = 7;
        editor.add(new JLabel("Sprecher"), gbc);

        gbc.gridx++;
        displayTalking = new JComboBox(new DisplayTalkingComboBoxModel());
        displayTalking.setEditable(false);
        displayTalking.addActionListener(this);
        editor.add(displayTalking, gbc);

        ////////////////////////////////////////////////////////////////////////////////////////////

        gbc.gridy++;
        buildTitle(editor, gbc, "Charaktere");

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("Charakter 1"), gbc);

        gbc.gridx++;
        displayCharacter1 = new JComboBox(new CharacterComboBoxModel());
        displayCharacter1.setEditable(false);
        displayCharacter1.addActionListener(this);
        editor.add(displayCharacter1, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets.top = 7;
        editor.add(new JLabel("Charakter 2"), gbc);

        gbc.gridx++;
        displayCharacter2 = new JComboBox(new CharacterComboBoxModel());
        displayCharacter2.setEditable(false);
        displayCharacter2.addActionListener(this);
        editor.add(displayCharacter2, gbc);

        ////////////////////////////////////////////////////////////////////////////////////////////

        updateData();

        ////////////////////////////////////////////////////////////////////////////////////////////

        gbc.gridy++;
        buildTitle(editor, gbc, "Eigenschaften");

        gbc.gridx = 0;
        gbc.gridy++;
        editor.add(new JLabel("Soundeffekt"), gbc);

        gbc.gridx++;
        gbc.gridwidth = 2;
        soundTextField = new JTextField(statement.getSound());
        soundTextField.addActionListener(this);
        editor.add(soundTextField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets.top = 20;
        saveButton = new JButton("Übernehmen");
        saveButton.addActionListener(this);
        editor.add(saveButton, gbc);

        ////////////////////////////////////////////////////////////////////////////////////////////

        gbc.gridy++;
        gbc.insets.top = 0;
        removeButton = new MetalGradientButton("Statement entfernen");
        removeButton.addActionListener(this);
        removeButton.setBackground(Color.RED.darker());
        removeButton.setEnabled(canRemove);
        editor.add(removeButton, gbc);

        gbc.gridy++;
        gbc.insets.top = 20;
        editor.add(new JLabel("Anzahl Texte: " + statement.getTexts().getText().size(), JLabel.CENTER), gbc);

        gbc.gridy++;
        gbc.insets.top = 0;
        addTextButton = new MetalGradientButton("Text hinzufügen");
        addTextButton.addActionListener(this);
        addTextButton.setBackground(Color.getHSBColor(0.370588f, 0.24f, 1.0f));
        editor.add(addTextButton, gbc);

        return editor;
    }

    private void buildTitle(JPanel editor, GridBagConstraints gbc, String title)
    {
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets.top = 15;
        editor.add(new JLabel("<html><font size=\"5\" color=\"#333\">" + title + "</font></html>"), gbc);
        gbc.gridwidth = 1;
        gbc.insets.top = 0;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {

        if (event.getSource() == displayModeNothing)
        {
            saveDisplayMode(DisplayModeEnum.NOTHING);
        }
        else if (event.getSource() == displayModePlayerOnly)
        {
            saveDisplayMode(DisplayModeEnum.PLAYER_ONLY);
        }
        else if (event.getSource() == displayModePlayerAndCharacter)
        {
            saveDisplayMode(DisplayModeEnum.PLAYER_AND_CHARACTER);
        }
        else if (event.getSource() == displayModeCharacter)
        {
            saveDisplayMode(DisplayModeEnum.CHARACTER);
        }
        else if (event.getSource() == displayModeTwoCharacters)
        {
            saveDisplayMode(DisplayModeEnum.TWO_CHARACTERS);
        }
        else if (event.getSource() == displayPositionSwap)
        {
            swapPosition();
        }
        else if (event.getSource() == displayTalking)
        {
            saveTalking();
        }
        else if (event.getSource() == displayCharacter1)
        {
            saveCharacter1();
        }
        else if (event.getSource() == displayCharacter2)
        {
            saveCharacter2();
        }
        else if (event.getSource() == soundTextField)
        {
            saveSound();
        }
        else if (event.getSource() == saveButton)
        {
            saveSound();
        }
        else if (event.getSource() == removeButton && canRemove)
        {
            int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                    "Wollen Sie wirklich dieses Statement löschen?", "Statement löschen", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                statementNode.getDialog().getStatement().remove(statementNode.getStatement());
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) statementNode.getParent();
                parent.remove(statementNode);
                editor.updateTree(parent);
            }
        }
        else if (event.getSource() == addTextButton)
        {
            String newText = "Neuer Text";

            int id = statementNode.getStatement().getTexts().getText().size();
            statementNode.getStatement().getTexts().getText().add(id, newText);

            statementNode.add(editor.new TextNode(this.statementNode.getDialog(), statementNode.getStatement(), newText, id));

            this.editor.updateTree(this.statementNode);
        }
    }

    private PlayerType createPlayer(DisplayModePosition position)
    {
        PlayerType player = new PlayerType();
        player.setPosition(position.getXmlValue());
        return player;
    }

    private DisplayType createCharacter(DisplayModePosition position)
    {
        DisplayType display = new DisplayType();
        display.setPosition(position.getXmlValue());
        display.setValue("");
        return display;
    }

    private void saveDisplayMode(DisplayModeEnum displayMode)
    {
        if (displayMode == this.displayMode) return;
        this.displayMode = displayMode;

        StatementType statement = statementNode.getStatement();

        switch (displayMode)
        {
            case NOTHING:
                statement.setPlayer(null);
                statement.getDisplay().clear();
                break;

            case PLAYER_ONLY:
                statement.setPlayer(createPlayer(DisplayModePosition.LEFT));
                statement.getDisplay().clear();
                break;

            case PLAYER_AND_CHARACTER:
                statement.setPlayer(createPlayer(DisplayModePosition.LEFT));
                statement.getDisplay().clear();
                statement.getDisplay().add(createCharacter(DisplayModePosition.RIGHT));
                break;

            case CHARACTER:
                statement.setPlayer(null);
                statement.getDisplay().clear();
                statement.getDisplay().add(createCharacter(DisplayModePosition.LEFT));
                break;

            case TWO_CHARACTERS:
                statement.setPlayer(null);
                statement.getDisplay().clear();
                statement.getDisplay().add(createCharacter(DisplayModePosition.LEFT));
                statement.getDisplay().add(createCharacter(DisplayModePosition.RIGHT));
                break;
        }

        updateData();
    }

    private void swapPosition()
    {
        StatementType statement = statementNode.getStatement();

        if (statement.getPlayer() != null)
        {
            if (statement.getPlayer().getPosition().equals(DisplayModePosition.LEFT.getXmlValue()))
            {
                statement.getPlayer().setPosition(DisplayModePosition.RIGHT.getXmlValue());
            } else
            {
                statement.getPlayer().setPosition(DisplayModePosition.LEFT.getXmlValue());
            }
        }

        for (DisplayType displayType : statement.getDisplay())
        {
            if (displayType.getPosition().equals(DisplayModePosition.LEFT.getXmlValue()))
            {
                displayType.setPosition(DisplayModePosition.RIGHT.getXmlValue());
            } else
            {
                displayType.setPosition(DisplayModePosition.LEFT.getXmlValue());
            }
        }

        updatePosition(statement);
    }

    private void saveTalking()
    {
        StatementType statement = statementNode.getStatement();
        DisplayTalkingComboBoxModel talkingModel = (DisplayTalkingComboBoxModel) displayTalking.getModel();

        DisplayTalkingEnum talkingEnum = talkingModel.getTalking();

        switch (talkingEnum)
        {
            case NOBODY:
                statement.setTalking("");
                break;
            case PLAYER:
                statement.setTalking("#player#");
                break;
            case CHARACTER_1:
                if (statement.getDisplay().size() < 1)
                    statement.setTalking("");
                else
                    statement.setTalking(statement.getDisplay().get(0).getValue());
                break;
            case CHARACTER_2:
                if (statement.getDisplay().size() < 2)
                    statement.setTalking("");
                else
                    statement.setTalking(statement.getDisplay().get(1).getValue());
                break;
        }

        updateTalking(statement);
    }

    private void saveCharacter1()
    {
        StatementType statement = statementNode.getStatement();
        CharacterComboBoxModel characterModel = (CharacterComboBoxModel) displayCharacter1.getModel();

        switch (displayMode)
        {

            case NOTHING:
            case PLAYER_ONLY:
                break;

            case PLAYER_AND_CHARACTER:
            case CHARACTER:
            case TWO_CHARACTERS:
                if (statement.getDisplay().size() >= 1)
                {
                    String oldId = statement.getDisplay().get(0).getValue();
                    String newId = characterModel.getSelectedId();

                    if (!newId.isEmpty())
                        statement.getDisplay().get(0).setValue(newId);

                    if (!oldId.equals(newId) && !newId.isEmpty() && !oldId.isEmpty() && statement.getTalking().equals(oldId))
                        statement.setTalking(newId);
                }
                break;
        }

        updateCharacters(statement);
        updateTalking(statement);
        updatePosition(statement);
    }

    private void saveCharacter2()
    {
        StatementType statement = statementNode.getStatement();
        CharacterComboBoxModel characterModel = (CharacterComboBoxModel) displayCharacter2.getModel();

        switch (displayMode)
        {

            case NOTHING:
            case PLAYER_ONLY:
            case PLAYER_AND_CHARACTER:
            case CHARACTER:
                break;

            case TWO_CHARACTERS:
                if (statement.getDisplay().size() >= 2)
                {
                    String oldId = statement.getDisplay().get(1).getValue();
                    String newId = characterModel.getSelectedId();

                    if (!newId.isEmpty())
                        statement.getDisplay().get(1).setValue(newId);

                    if (!oldId.equals(newId) && !newId.isEmpty() && !oldId.isEmpty() && statement.getTalking().equals(oldId))
                        statement.setTalking(newId);
                }
                break;
        }

        updateCharacters(statement);
        updateTalking(statement);
        updatePosition(statement);
    }

    private void saveSound()
    {
        statementNode.getStatement().setSound(soundTextField.getText());
    }

    private void updateData()
    {
        StatementType statement = statementNode.getStatement();

        updatePosition(statement);
        updateTalking(statement);
        updateCharacters(statement);
    }

    private void updatePosition(StatementType statement)
    {
        String left = "Links: ";
        String right = "Rechts: ";

        if (statement.getPlayer() != null)
        {
            if (statement.getPlayer().getPosition().equals(DisplayModePosition.LEFT.getXmlValue()))
            {
                left += "Spieler";
            } else {
                right += "Spieler";
            }
        }

        for (DisplayType displayType : statement.getDisplay())
        {
            String displayFormat = String.format("Char %d -> %s", statement.getDisplay().indexOf(displayType) + 1, displayType.getValue());
            if (displayType.getPosition().equals(DisplayModePosition.LEFT.getXmlValue()))
            {
                left += displayFormat;
            } else {
                right += displayFormat;
            }
        }

        displayPositionLeft.setText(left);
        displayPositionRight.setText(right);
    }

    private void updateTalking(StatementType statement)
    {
        String talking = statement.getTalking();
        DisplayTalkingComboBoxModel talkingModel = (DisplayTalkingComboBoxModel) displayTalking.getModel();

        if (talking.isEmpty())
        {
            displayTalking.setSelectedIndex(talkingModel.getTalkingIndex(DisplayTalkingEnum.NOBODY));
        }
        else if (talking.equals("#player#"))
        {
            if (displayMode == DisplayModeEnum.PLAYER_ONLY || displayMode == DisplayModeEnum.PLAYER_AND_CHARACTER)
            {
                displayTalking.setSelectedIndex(talkingModel.getTalkingIndex(DisplayTalkingEnum.PLAYER));
            } else {
                statement.setTalking("");
                displayTalking.setSelectedIndex(0);
            }
        }
        else
        {
            displayTalking.setSelectedIndex(0);

            for (DisplayType displayType : statement.getDisplay())
            {
                if (displayType.getValue().equals(talking))
                {
                    int characterNr = statement.getDisplay().indexOf(displayType);

                    if (
                            characterNr == 0 && (
                                    displayMode == DisplayModeEnum.PLAYER_AND_CHARACTER ||
                                    displayMode == DisplayModeEnum.CHARACTER ||
                                    displayMode == DisplayModeEnum.TWO_CHARACTERS))
                    {
                        displayTalking.setSelectedIndex(talkingModel.getTalkingIndex(DisplayTalkingEnum.CHARACTER_1));
                    }
                    else if (characterNr == 1 && displayMode == DisplayModeEnum.TWO_CHARACTERS)
                    {
                        displayTalking.setSelectedIndex(talkingModel.getTalkingIndex(DisplayTalkingEnum.CHARACTER_2));
                    }
                    else
                    {
                        statement.setTalking("");
                    }
                    break;
                }
            }
        }

        displayTalking.updateUI();
        updateTree(statementNode);
    }

    private void updateCharacters(StatementType statement)
    {
        CharacterComboBoxModel character1Model = (CharacterComboBoxModel) displayCharacter1.getModel();
        CharacterComboBoxModel character2Model = (CharacterComboBoxModel) displayCharacter2.getModel();

        if (statement.getDisplay().size() >= 1)
        {
            String char1Id = statement.getDisplay().get(0).getValue();
            displayCharacter1.setSelectedIndex(character1Model.getIndexForId(char1Id));
        }

        if (statement.getDisplay().size() >= 2)
        {
            String char2Id = statement.getDisplay().get(1).getValue();
            displayCharacter2.setSelectedIndex(character2Model.getIndexForId(char2Id));
        }

        switch (displayMode)
        {
            case NOTHING:
            case PLAYER_ONLY:
                displayCharacter1.setEnabled(false);
                displayCharacter2.setEnabled(false);
                break;

            case PLAYER_AND_CHARACTER:
            case CHARACTER:
                displayCharacter1.setEnabled(true);
                displayCharacter2.setEnabled(false);
                break;

            case TWO_CHARACTERS:
                displayCharacter1.setEnabled(true);
                displayCharacter2.setEnabled(true);
                break;
        }

        displayCharacter1.updateUI();
        displayCharacter2.updateUI();
    }

    private DisplayModeEnum getDisplayMode(StatementType statement)
    {
        DisplayModeEnum displayMode = DisplayModeEnum.NOTHING;

        if (statement.getPlayer() != null)
        {
            if (statement.getDisplay().size() == 0)
            {
                displayMode = DisplayModeEnum.PLAYER_ONLY;
            } else {
                displayMode = DisplayModeEnum.PLAYER_AND_CHARACTER;
            }
        } else {
            if (statement.getDisplay().size() == 1)
            {
                displayMode = DisplayModeEnum.CHARACTER;
            }
            else if (statement.getDisplay().size() == 2)
            {
                displayMode = DisplayModeEnum.TWO_CHARACTERS;
            }
        }

        return displayMode;
    }

    private class DisplayTalkingComboBoxModel implements ComboBoxModel
    {
        private int selectedIndex = -1;

        private String[] talking = new String[]{
                "Niemand",
                "Spieler",
                "Charakter 1",
                "Charakter 2"

        };

        @Override
        public void setSelectedItem(Object anItem)
        {
            for (int i = 0; i < talking.length; i++)
            {
                if (talking[i].equals(anItem))
                {
                    selectedIndex = i;
                    break;
                }
            }
        }

        @Override
        public Object getSelectedItem()
        {
            if (selectedIndex >= 0)
            {
                return talking[selectedIndex];
            } else {
                return "";
            }
        }

        @Override
        public int getSize()
        {
            switch (StatementEditorPanel.this.displayMode)
            {
                case NOTHING:
                    return 1;
                case PLAYER_ONLY:
                    return 2;
                case PLAYER_AND_CHARACTER:
                    return 3;
                case CHARACTER:
                    return 2;
                case TWO_CHARACTERS:
                    return 3;
            }

            return 1;
        }

        @Override
        public Object getElementAt(int index)
        {
            return talking[indexToInternal(index)];
        }

        private int indexToInternal(int index)
        {
            if (index == 0)
                return 0;

            switch (StatementEditorPanel.this.displayMode)
            {
                case NOTHING:
                    return 0;
                case PLAYER_ONLY:
                case PLAYER_AND_CHARACTER:
                    return index;
                case CHARACTER:
                case TWO_CHARACTERS:
                    return index + 1;
            }

            return 0;
        }

        public DisplayTalkingEnum getTalking()
        {
            switch (selectedIndex)
            {
                case 0:
                    return DisplayTalkingEnum.NOBODY;

                case 1:
                    return DisplayTalkingEnum.PLAYER;

                case 2:
                    return DisplayTalkingEnum.CHARACTER_1;

                case 3:
                    return DisplayTalkingEnum.CHARACTER_2;
            }

            return DisplayTalkingEnum.NOBODY;
        }

        public int getTalkingIndex(DisplayTalkingEnum talking)
        {
            boolean characterMode   =                   StatementEditorPanel.this.displayMode == DisplayModeEnum.CHARACTER;
            characterMode           = characterMode ||  StatementEditorPanel.this.displayMode == DisplayModeEnum.TWO_CHARACTERS;

            switch (talking)
            {
                case NOBODY:
                    return 0;

                case PLAYER:
                    return 1;

                case CHARACTER_1:
                    return characterMode ? 1 : 2;

                case CHARACTER_2:
                    return characterMode ? 2 : 0;

            }

            return 0;
        }

        @Override
        public void addListDataListener(ListDataListener l)
        {
        }

        @Override
        public void removeListDataListener(ListDataListener l)
        {
        }
    }

    private class CharacterComboBoxModel implements ComboBoxModel
    {
        private CharactersType characters;
        private int selectedIndex = 0;

        public CharacterComboBoxModel()
        {
            characters = StatementEditorPanel.this.editor.getLevel().getCharacters();
        }

        @Override
        public void setSelectedItem(Object anItem)
        {
            for (int i = 0; i < characters.getCharacter().size(); i++)
            {
                if (characters.getCharacter().get(i).getId().equals(anItem))
                {
                    selectedIndex = i;
                    break;
                }
            }
        }

        @Override
        public Object getSelectedItem()
        {
            return characters.getCharacter().get(selectedIndex).getId();
        }

        @Override
        public int getSize()
        {
            return characters.getCharacter().size();
        }

        @Override
        public Object getElementAt(int index)
        {
            return characters.getCharacter().get(index).getId();
        }

        @Override
        public void addListDataListener(ListDataListener l)
        {
        }

        @Override
        public void removeListDataListener(ListDataListener l)
        {
        }

        public String getSelectedId()
        {
            return characters.getCharacter().get(selectedIndex).getId();
        }

        public int getIndexForId(String searchId)
        {
            for (int i = 0; i < characters.getCharacter().size(); i++)
            {
                if (characters.getCharacter().get(i).getId().equals(searchId))
                {
                    return i;
                }
            }

            return 0;
        }
    }

    private enum DisplayTalkingEnum
    {
        NOBODY,
        PLAYER,
        CHARACTER_1,
        CHARACTER_2
    }

    private enum DisplayModeEnum {
        NOTHING,
        PLAYER_ONLY,
        PLAYER_AND_CHARACTER,
        CHARACTER,
        TWO_CHARACTERS
    }

    private enum DisplayModePosition {

        LEFT("left"),
        RIGHT("right");

        private final String xmlValue;

        DisplayModePosition(String xmlValue)
        {
            this.xmlValue = xmlValue;
        }

        public String getXmlValue()
        {
            return this.xmlValue;
        }
    }
}
