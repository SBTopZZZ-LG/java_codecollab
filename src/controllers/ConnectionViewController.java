package controllers;

import components.PropertyChangeSupported;
import models.FileStore;
import models.ProtocolModel;
import utilities.RequestHandlers;
import utilities.TCPClient;
import views.ConnectionView;
import views.HomeView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class ConnectionViewController extends PropertyChangeSupported {
    public final ConnectionView parent;

    public static final Pattern IP_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    public static final Pattern HOSTNAME_PATTERN = Pattern.compile("^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$");

    public static final Pattern HOSTPORT_PATTERN = Pattern.compile("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");

    public ConnectionViewController(final ConnectionView parent) {
        this.parent = parent;

        addListeners();
    }

    private void addListeners() {
        parent.connectBtn.addActionListener((__) -> {
            final String hostName = parent.hostNameField.getText();
            final String hostPort = parent.hostPortField.getText();

            if (!IP_PATTERN.matcher(hostName).matches() && !HOSTNAME_PATTERN.matcher(hostName).matches()) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Specified Host name is invalid. Please enter a valid IPv4 address or a host address.",
                        "Invalid Host Name",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (!HOSTPORT_PATTERN.matcher(hostPort).matches()) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Specified Host port is invalid. Please enter a valid port in the range 0-65535.",
                        "Invalid Host Port",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            final List<RequestHandlers.PostRequestHandler> postHandlers = List.of(
                    RequestHandlers::postChatMsg
            );

            ProtocolModel.instance = new ProtocolModel() {{
                init(hostName, Integer.parseInt(hostPort), new ProtocolListener() {
                    @Override
                    public void onHostStatusUpdate(TCPClient.Options options, HostStatusUpdateMode mode) {
                        if (mode == HostStatusUpdateMode.Connected) {
                            System.out.println("Connected to host");

                            try {
                                FileStore.instance = new FileStore();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            new HomeView() {{
                                addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        super.windowClosing(e);

                                        try {
                                            ProtocolModel.instance.client.socket.close();
                                            ProtocolModel.instance = null;
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        } finally {
                                            parent.setVisible(true);
                                        }
                                    }
                                });
                            }};

                            parent.setVisible(false);
                        } else {
                            System.out.println("Disconnected from host");

                            if (FileStore.instance != null) {
                                try {
                                    FileStore.instance.unlink("");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onGetData(String requestId, String params, TCPClient.Options options) {}

                    @Override
                    public void onPostData(String requestId, String params, List<String> payloads, TCPClient.Options options) {
                        System.out.println("\nPost RequestId: " + requestId);
                        System.out.println("Params: " + params);
                        System.out.println("Payloads:");
                        payloads.forEach(payload -> System.out.println("- \"" + payload + "\""));

                        for (final RequestHandlers.PostRequestHandler handler : postHandlers)
                            if (handler.handler(requestId, params, payloads, options)) break;
                    }

                    @Override
                    public void onSayData(String methodId, String params) {}
                });
            }};
        });
    }
}
