package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class BasePanel extends JPanel {

    protected final DialogEditor editor;

    public BasePanel(LayoutManager layoutManager, DialogEditor editor)
    {
        super(layoutManager);

        this.editor = editor;
    }

    protected final void updateTree(DefaultMutableTreeNode node)
    {
        editor.updateTree(node);
    }
}
