package views;

import components.CenteredJFrame;
import components.DirTreeCellRenderer;
import components.RSyntaxTextEditor;
import controllers.HomeViewController;
import models.FileWrapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class HomeView extends CenteredJFrame {
    public final HomeViewController controller;

    public final DefaultMutableTreeNode jTreeRoot = new DefaultMutableTreeNode("");
    public JTree jTree = new JTree(jTreeRoot) {{
        setCellRenderer(new DirTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setEnabled(false);
        setMinimumSize(new Dimension(100, getMinimumSize().height));
    }};
    public JScrollPane jTreeScrollPane = new JScrollPane(jTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    public RSyntaxTextEditor editor = new RSyntaxTextEditor() {{
        container.setMinimumSize(new Dimension(100, container.getMinimumSize().height));
    }};

    public JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jTreeScrollPane, editor.container) {{
        setOneTouchExpandable(true);
        setDividerLocation(150);
    }};

    public String activeFilePath = null, activeOriginalFilePath = null;
    public FileWrapper activeFileWrapper = null;

    public HomeView() {
        super(new Dimension(850, 600));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("insets 0 0 0 0"));
        setTitle("CodeCollab");

        initComponents();
        controller = new HomeViewController(this);

        setVisible(true);
    }

    public void initComponents() {
        add(jSplitPane, "width 100%, height 100%");
    }
}
