package controllers;

import components.PropertyChangeSupported;
import models.FileStore;
import models.FileWrapper;
import models.ProtocolModel;
import models.Synchronized;
import utilities.Commands;
import utilities.CommonUtils;
import views.HomeView;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HomeViewController extends PropertyChangeSupported {
    public final HomeView parent;

    private final Synchronized<Boolean> aSynchronized = new Synchronized<>(false);

    public HomeViewController(final HomeView parent) {
        this.parent = parent;

        setupListeners();
    }

    public void setupListeners() {
        parent.jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // TODO: That weird error

                    final TreePath path = parent.jTree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) return;

                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    final FileWrapper wrapper = (FileWrapper) node.getUserObject();

                    parent.activeFileWrapper = wrapper;

                    if (!FileStore.instance.exists(wrapper.absolutePath)) {
                        try {
                            final BufferedOutputStream bos = FileStore.instance.touchFile(wrapper.absolutePath);
                            Commands.getBinary(wrapper.absolutePath, bos)
                                    .whenComplete((__, throwable2) -> aSynchronized.get(___ -> {
                                        try {
                                            bos.close();

                                            final BufferedOutputStream bos2 = FileStore.instance.touchFile(wrapper.absolutePath + ".original");
                                            bos2.write(FileStore.instance.getContent(wrapper.absolutePath).getBytes(StandardCharsets.UTF_8));
                                            bos2.close();

                                            setActiveFile(wrapper.absolutePath);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                        return null;
                                    }));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        try {
                            setActiveFile(wrapper.absolutePath);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        ProtocolModel.instance.invokeLater(() -> Commands.getList("/")
                .whenComplete(((fileWrappers, throwable) -> {
                    if (!fileWrappers.wasSuccess) {
                        JOptionPane.showMessageDialog(parent,
                                "Failed to retrieve the root directory for the project. Please check your internet connection and try again later.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);

                        return;
                    }
                    assert fileWrappers.data != null;

                    parent.jTreeRoot.setUserObject(fileWrappers.data[0].name + " [Project]");

                    FileWrapper.sort(fileWrappers.data);
                    for (FileWrapper fileWrapper : fileWrappers.data)
                        if (!fileWrapper.absolutePath.equals("/"))
                            parent.jTreeRoot.add(new DefaultMutableTreeNode(fileWrapper));

                    ((DefaultTreeModel) parent.jTree.getModel()).reload();

                    parent.jTree.setEnabled(true);
                })));

        parent.editor.textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "autoSaveCustomBind");
        parent.editor.textArea.getActionMap().put("autoSaveCustomBind", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parent.activeFilePath == null || !parent.editor.textArea.isEnabled())
                    return;

                parent.activeFileWrapper.fileStatus = FileWrapper.FileStatus.SAVING;
                parent.jTree.setEnabled(false);
                parent.editor.textArea.setEnabled(false);

                try {
                    FileStore.instance.setContent(parent.activeFilePath, parent.editor.textArea.getText());

                    final BufferedInputStream bis = FileStore.instance.openFile(parent.activeOriginalFilePath);
                    Commands.postUpdatedBinary(parent.editor.textArea.getText(), parent.activeFilePath, bis, FileStore.instance.getHash(parent.activeOriginalFilePath))
                            .whenComplete((merged, throwable) -> {
                                parent.jTree.setEnabled(true);
                                parent.editor.textArea.setEnabled(true);

                                try {
                                    bis.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                                if (!merged.wasSuccess) {
                                    JOptionPane.showMessageDialog(parent,
                                            "Saving operation failed. Please check your internet connection and try again later.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);

                                    return;
                                }

                                parent.activeFileWrapper.fileStatus = FileWrapper.FileStatus.IDLE;

                                try {
                                    if (merged.data != null) {
                                        FileStore.instance.setContent(parent.activeFilePath, merged.data);
                                        FileStore.instance.setContent(parent.activeOriginalFilePath, merged.data);
                                    } else {
                                        FileStore.instance.setContent(parent.activeFilePath, parent.editor.textArea.getText());
                                        FileStore.instance.setContent(parent.activeOriginalFilePath, parent.editor.textArea.getText());
                                    }

                                    setActiveFile(parent.activeFilePath);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public void setActiveFile(final String path) throws IOException {
        if (parent.activeFilePath != null)
            FileStore.instance.setContent(parent.activeFilePath, parent.editor.textArea.getText());

        parent.editor.textArea.setText(FileStore.instance.getContent(path));
        parent.editor.setSyntaxEditingStyle(CommonUtils.getFileExtension(path));

        parent.activeOriginalFilePath = path + ".original";
        parent.activeFilePath = path;
        parent.setTitle("CodeCollab - " + parent.activeFilePath);
    }
}
