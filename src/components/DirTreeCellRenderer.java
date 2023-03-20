package components;

import models.FileWrapper;
import utilities.Commands;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DirTreeCellRenderer extends DefaultTreeCellRenderer {
    private final Map<DefaultMutableTreeNode, Boolean> transactions = new HashMap<>();

    public DirTreeCellRenderer() {
        setFont(new JLabel().getFont().deriveFont(15f));
        setHorizontalTextPosition(JLabel.LEFT);

        setBackgroundSelectionColor(Color.BLUE);
        setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object value2 = node.getUserObject();

        if (value2 instanceof final FileWrapper treeFile) {
            setText(treeFile.name);
            if (!treeFile.isFile) {
                if (!expanded) {
                    if (node.getChildCount() == 0)
                        node.add(new DefaultMutableTreeNode("<loading>"));
                } else if (!transactions.containsKey(node)) {
                    transactions.put(node, true);

                    Commands.getList(treeFile.absolutePath)
                            .whenComplete(((fileWrappers, throwable) -> {
                                if (!fileWrappers.wasSuccess) {
                                    JOptionPane.showMessageDialog(tree,
                                            "Failed to retrieve contents for path \"" + treeFile.absolutePath + "\". Please check your internet connection and try again later.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);

                                    return;
                                }
                                assert fileWrappers.data != null;

                                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent((MutableTreeNode) node.getFirstChild());

                                FileWrapper.sort(fileWrappers.data);
                                for (FileWrapper fileWrapper : fileWrappers.data)
                                    if (!fileWrapper.absolutePath.equals(treeFile.absolutePath))
                                        ((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(fileWrapper), node, node.getChildCount());

                                tree.expandRow(row);
                            }));
                }
            }
        } else
            setText(value2.toString());

        if (selected) {
            super.setBackground(getBackgroundSelectionColor());
            setForeground(getTextSelectionColor());
        } else {
            super.setBackground(getBackgroundNonSelectionColor());
            setForeground(getTextNonSelectionColor());
        }

        return this;
    }
}
