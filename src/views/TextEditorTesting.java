package views;

import net.miginfocom.swing.MigLayout;
import components.CenteredJFrame;
import components.RSyntaxTextEditor;

import javax.swing.*;
import java.awt.*;

public class TextEditorTesting extends CenteredJFrame {
    public RSyntaxTextEditor editor;

    public TextEditorTesting() {
        super(new Dimension(600, 600));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("insets 0 0 0 0"));
        setTitle("CodeCollab");

        initComponents();

        setVisible(true);
    }

    public void initComponents() {
        editor = new RSyntaxTextEditor();
        add(editor.container, "width 100%, height 100%");
    }
}
