package de.entwicklerpages.java.schoolgame.tools.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class DialogEditor extends JPanel {

    private JTree treeView = null;
    private JButton saveButton = null;
    private JButton loadButton = null;

    public DialogEditor()
    {
        super(new BorderLayout());

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Level");

        DefaultMutableTreeNode charactersNode = new DefaultMutableTreeNode( "Charaktere" );
        root.add( charactersNode );

        DefaultMutableTreeNode dialogNode = new DefaultMutableTreeNode(  "Dialoge" );
        root.add( dialogNode );

        for ( int nodeCnt = 0; nodeCnt < 4; nodeCnt++ )
        {
            DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode( "Knoten " + nodeCnt );
            dialogNode.add( dmtn );

            for ( int leafCnt = 1; leafCnt < 4; leafCnt++ )
                dmtn.add( new DefaultMutableTreeNode( "Blatt " + (nodeCnt * 3 + leafCnt) ) );
        }


        treeView = new JTree(root);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(new JScrollPane(treeView));
        splitPane.add(new JPanel());

        add(splitPane, BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new GridLayout(1, 2));

        loadButton = new JButton("Laden");
        saveButton = new JButton("Speichern");

        saveBar.add(loadButton);
        saveBar.add(saveButton);

        add(saveBar, BorderLayout.SOUTH);
    }

}
