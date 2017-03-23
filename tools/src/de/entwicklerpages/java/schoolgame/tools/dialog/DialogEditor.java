package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.entwicklerpages.java.schoolgame.game.dialog.CharacterType;
import de.entwicklerpages.java.schoolgame.game.dialog.CharactersType;
import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.DialogsType;
import de.entwicklerpages.java.schoolgame.game.dialog.Level;
import de.entwicklerpages.java.schoolgame.game.dialog.StatementType;

public class DialogEditor extends JPanel implements ActionListener {

    private static final String TREE_CHARACTER = "Charaktere";
    private static final String TREE_DIALOG = "Dialoge";

    private Level level = null;
    private File lastDir = null;

    private JTree treeView = null;
    private JButton saveButton = null;
    private JButton loadButton = null;

    private JPanel containerPanel = new JPanel(new BorderLayout());
    private JPanel emptyPanel = new JPanel();
    private JPanel createLevelPanel = null;
    private FileFilter filter = new FileNameExtensionFilter("Dialog XML Datei", "xml");

    private DefaultMutableTreeNode root = null;

    public DialogEditor()
    {
        super(new BorderLayout());

        root = new DefaultMutableTreeNode("Level");
        buildTree();
        treeView = new JTree(root);
        addTreeChangeListener();

        fillCreateLevelPanel();
        containerPanel.add(createLevelPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(new JScrollPane(treeView));
        splitPane.add(containerPanel);
        splitPane.setResizeWeight(0.25);

        add(splitPane, BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new GridLayout(1, 2));

        loadButton = new JButton("Laden");
        loadButton.addActionListener(this);

        saveButton = new JButton("Speichern");
        saveButton.addActionListener(this);
        saveButton.setEnabled(false);

        saveBar.add(loadButton);
        saveBar.add(saveButton);

        add(saveBar, BorderLayout.SOUTH);
    }

    private void addTreeChangeListener()
    {
        treeView.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override public void valueChanged(TreeSelectionEvent event)
            {
                TreePath path = event.getNewLeadSelectionPath();

                if (level == null)
                {
                    replaceView(createLevelPanel);
                    return;
                }

                if (path == null || path.getPath().length == 0)
                {
                    replaceView(emptyPanel);
                } else {

                    if (path.getPathCount() == 1)
                    {
                        replaceView(emptyPanel);
                    }
                    else if (path.getPathCount() == 2)
                    {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        if (node.getUserObject().equals(TREE_CHARACTER))
                        {
                            replaceView(new CharactersOverviewPanel(DialogEditor.this, node, level.getCharacters()));
                        } else {
                            replaceView(new DialogsOverviewPanel(DialogEditor.this, node, level.getDialogs()));
                        }
                    }
                    else if (path.getPathCount() >= 3)
                    {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        if (node instanceof NodeData)
                        {
                            NodeData data = (NodeData) node;
                            JPanel editor = data.getEditor();
                            if (editor != null)
                            {
                                replaceView(editor);
                            }
                        }
                    }
                }
            }
        });
    }

    private void fillCreateLevelPanel()
    {
        createLevelPanel = new JPanel(new BorderLayout());

        JButton createLevelButton = new JButton("Neues Level anlegen");
        createLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildLevel();
                treeView.setSelectionRow(0);
            }
        });

        createLevelPanel.add(new JLabel("Du musst zuerst ein leeres Level anlegen.", JLabel.CENTER), BorderLayout.CENTER);
        createLevelPanel.add(createLevelButton, BorderLayout.SOUTH);
    }

    private void buildLevel()
    {
        if (level != null)
            return;

        level = new Level();

        level.setAtlas("");
        level.setDialogs(new DialogsType());

        CharactersType charactersType = new CharactersType();

        CharacterType defaultCharacter = new CharacterType();
        defaultCharacter.setId("default");
        defaultCharacter.setImage("no");
        defaultCharacter.setTitle("Default");

        charactersType.getCharacter().add(defaultCharacter);

        level.setCharacters(charactersType);

        saveButton.setEnabled(true);
        rebuildTree();
    }

    private void replaceView(Component view)
    {
        containerPanel.removeAll();
        containerPanel.add(view);
        containerPanel.updateUI();
    }

    private void buildTree()
    {
        root.removeAllChildren();

        if (level == null)
            return;

        DefaultMutableTreeNode charactersNode = new DefaultMutableTreeNode(TREE_CHARACTER);
        root.add(charactersNode);

        List<CharacterType> characters = level.getCharacters().getCharacter();
        for (CharacterType character : characters) {
            charactersNode.add(new CharacterNode(character));
        }

        DefaultMutableTreeNode dialogsNode = new DefaultMutableTreeNode(TREE_DIALOG);
        root.add(dialogsNode);

        List<DialogType> dialogs = level.getDialogs().getDialog();
        for (DialogType dialog : dialogs) {
            DefaultMutableTreeNode dialogTreeNode = new DialogNode(dialog);
            dialogsNode.add(dialogTreeNode);

            List<StatementType> statements = dialog.getStatement();
            for (StatementType statement : statements)
            {
                DefaultMutableTreeNode statementTreeNode = new StatementNode(dialog, statement);
                dialogTreeNode.add(statementTreeNode);

                List<String> texts = statement.getTexts().getText();
                for (String text : texts)
                {
                    statementTreeNode.add(new TextNode(dialog, statement, text, texts.indexOf(text)));
                }
            }
        }
    }

    public void rebuildTree()
    {
        buildTree();

        if (treeView != null)
            ((DefaultTreeModel) treeView.getModel()).reload(root);
    }

    public void updateTree(DefaultMutableTreeNode node)
    {
        ((DefaultTreeModel) treeView.getModel()).nodeChanged(node);
        ((DefaultTreeModel) treeView.getModel()).reload(node);
    }

    private void loadLevel()
    {
        JFileChooser chooser = new JFileChooser(lastDir == null ? DialogDataHelper.getAssetDirIfFound() : lastDir);
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setMultiSelectionEnabled(false);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            saveButton.setEnabled(false);
            replaceView(createLevelPanel);
            level = null;
            File selectedFile = chooser.getSelectedFile();
            try {
                level = DialogDataHelper.getDialogRoot(selectedFile);
                lastDir = selectedFile.getParentFile();
                saveButton.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Beim Laden ist ein Fehler aufgetreten!\n" + e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
            }
            rebuildTree();
        }
    }

    public Level getLevel()
    {
        return level;
    }

    private void saveFile()
    {
        if (level == null)
        {
            JOptionPane.showMessageDialog(this, "Erstellen oder laden Sie zuerst ein Level, bevor sie es speichern!\n", "Fehler!", JOptionPane.OK_OPTION);
            return;
        }

        JFileChooser chooser = new JFileChooser(lastDir == null ? DialogDataHelper.getAssetDirIfFound() : lastDir);
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setMultiSelectionEnabled(false);

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = chooser.getSelectedFile();

            if (!selectedFile.getAbsolutePath().endsWith(".xml"))
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xml");

            if (selectedFile.exists() && selectedFile.isDirectory())
            {
                JOptionPane.showMessageDialog(this, "Es existiert dort bereits ein Verzeichnis mit diesem Namen!\n", "Fehler!", JOptionPane.OK_OPTION);
                return;
            }

            if (selectedFile.exists())
            {
                result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                        "Es existiert bereits eine Datei mit diesem Namen.\n" +
                        "Wollen Sie wirklich die vorhandene Datei unwiederruflich überschreiben?", "Datei überschreiben", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try {
                DialogDataHelper.saveDialogRoot(selectedFile, level);
                lastDir = selectedFile.getParentFile();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Beim Speichern ist ein Fehler aufgetreten!\n" + e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == loadButton)
        {
            if (level != null)
            {
                int result = JOptionPane.showConfirmDialog(this, "Achtung!\n" +
                        "Es wird ein neues Level geladen.\n" +
                        "Ungespeicherte Änderungen gehen verloren!\n" +
                        "Wollen Sie wirklich ein anderes Level laden?", "Level laden", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    loadLevel();
                }
            } else {
                loadLevel();
            }
        }
        else if (event.getSource() == saveButton)
        {
            saveFile();
        }
    }

    public abstract class NodeData extends DefaultMutableTreeNode
    {
        public abstract JPanel getEditor();
    }

    public class CharacterNode extends NodeData
    {
        protected CharacterType character;

        public CharacterNode(CharacterType character) {
            this.character = character;
        }

        public CharacterType getCharacter() {
            return character;
        }

        @Override
        public JPanel getEditor() {
            return new CharacterEditorPanel(DialogEditor.this, this);
        }

        @Override
        public String toString() {
            return character.getId().length() == 0 ? "Charakter" : character.getId();
        }
    }

    public class DialogNode extends NodeData
    {
        protected DialogType dialog;

        public DialogNode(DialogType dialog) {
            this.dialog = dialog;
        }

        public DialogType getDialog() {
            return dialog;
        }

        @Override
        public JPanel getEditor() {
            return new DialogEditorPanel(this);
        }

        @Override
        public String toString() {
            return dialog.getName().length() == 0 ? "Dialog" : dialog.getName();
        }
    }

    public class StatementNode extends DialogNode
    {
        protected StatementType statement;

        public StatementNode(DialogType dialog, StatementType statement) {
            super(dialog);
            this.statement = statement;
        }

        public StatementType getStatement() {
            return statement;
        }

        @Override
        public JPanel getEditor() {
            return null;
        }

        @Override
        public String toString() {
            return statement.getTalking().length() == 0 ? "Statement" : "Statement by " + statement.getTalking();
        }
    }

    public class TextNode extends StatementNode
    {
        protected String text;
        protected int id;

        public TextNode(DialogType dialog, StatementType statement, String text, int id) {
            super(dialog, statement);
            this.text = text;
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public int getId() {
            return id;
        }

        @Override
        public JPanel getEditor() {
            return null;
        }

        @Override
        public String toString() {
            return "Text " + id;
        }
    }
}
