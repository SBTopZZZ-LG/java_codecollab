package views;

import components.CenteredJFrame;
import controllers.ConnectionViewController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ConnectionView extends CenteredJFrame {
    public static void main(String[] args) {
        new ConnectionView();
    }

    public final ConnectionViewController controller;

    public JLabel messageLabel = new JLabel("Please fill the host details in order to connect") {{
        setFont(getFont().deriveFont(Font.BOLD));
        setHorizontalAlignment(CENTER);
    }};

    public JLabel hostNameLabel = new JLabel("Host name: ");
    public JLabel hostPortLabel = new JLabel("Port: ");

    public JTextField hostNameField = new JTextField();
    public JTextField hostPortField = new JTextField();

    public JButton connectBtn = new JButton("Connect...");

    public ConnectionView() {
        super(new Dimension(400, 190));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("insets 20 20 20 20", "[][][]"));
        setResizable(false);
        setTitle("CodeCollab - Connect to Host");

        initComponents();
        controller = new ConnectionViewController(this);

        setVisible(true);
    }

    void initComponents() {
        add(messageLabel, "width 100%, wrap, spanx 3");

        add(hostNameLabel, "gapy 10");
        add(hostNameField, "width 100%, wrap, spanx 2");

        add(hostPortLabel);
        add(hostPortField, "width 100%, wrap, spanx 2");

        add(connectBtn, "cell 1 3");
    }
}
