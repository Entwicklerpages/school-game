package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.entwicklerpages.java.schoolgame.game.dialog.CharacterType;
import de.entwicklerpages.java.schoolgame.game.dialog.DialogType;
import de.entwicklerpages.java.schoolgame.game.dialog.Level;

public class DialogEditor extends JPanel implements ActionListener {

    private Level level = null;

    private JTree treeView = null;
    private JButton saveButton = null;
    private JButton loadButton = null;

    private JPanel containerPanel = new JPanel();

    private DefaultMutableTreeNode root = null;

    public DialogEditor()
    {
        super(new BorderLayout());

        root = new DefaultMutableTreeNode("Level");
        buildTree();
        treeView = new JTree(root);

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

        saveBar.add(loadButton);
        saveBar.add(saveButton);

        add(saveBar, BorderLayout.SOUTH);
    }

    private void buildTree()
    {
        root.removeAllChildren();

        if (level == null)
            return;

        DefaultMutableTreeNode charactersNode = new DefaultMutableTreeNode("Charaktere");
        root.add(charactersNode);

        List<CharacterType> characters = level.getCharacters().getCharacter();
        for (CharacterType character : characters) {
            charactersNode.add(new DefaultMutableTreeNode(character.getId().length() == 0 ? "Charakter" : character.getId()));
        }

        DefaultMutableTreeNode dialogsNode = new DefaultMutableTreeNode("Dialoge");
        root.add(dialogsNode);

        List<DialogType> dialogs = level.getDialogs().getDialog();
        for (DialogType dialog : dialogs) {
            dialogsNode.add(new DefaultMutableTreeNode(dialog.getName().length() == 0 ? "Dialog" : dialog.getName()));
        }
    }

    private void rebuildTree()
    {
        buildTree();

        if (treeView != null)
            ((DefaultTreeModel) treeView.getModel()).reload(root);
    }

    private void loadLevel()
    {
        FileFilter filter = new FileNameExtensionFilter("Dialog XML Datei", "xml");
        JFileChooser chooser = new JFileChooser(DialogDataHelper.getAssetDirIfFound());
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(true);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            level = null;
            File selectedFile = chooser.getSelectedFile();
            try {
                level = DialogDataHelper.getDialogRoot(selectedFile);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Beim Laden ist ein Fehler aufgetreten!\n" + e.getLocalizedMessage(), "Fehler!", JOptionPane.OK_OPTION);
            }
            rebuildTree();
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
    }
}
