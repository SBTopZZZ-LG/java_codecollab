package models;

import utilities.TCPClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolModel {
    public static ProtocolModel instance = null;

    public final TCPClient client;

    private final Pattern requestHeadPattern = Pattern.compile("^(?<method>[a-z]+) +(?<requestId>[a-z0-9]+)( +\"(?<params>.+)\")?$", Pattern.CASE_INSENSITIVE);
    private final Pattern payloadContentHeadPattern = Pattern.compile("^(?<length>\\d+)$", Pattern.CASE_INSENSITIVE);
    private final Pattern payloadEndPattern = Pattern.compile("^DONE$", Pattern.CASE_INSENSITIVE);

    private Thread protocolThread = null;
    private final Synchronized<Queue<Runnable>> callbacks = new Synchronized<>(new LinkedList<>());

    public enum HostStatusUpdateMode {
        Connected,
        Disconnected
    }
    public interface ProtocolListener {
        void onHostStatusUpdate(final TCPClient.Options options, final HostStatusUpdateMode mode);
        void onGetData(final String requestId, final String params, final TCPClient.Options options);
        void onPostData(final String requestId, final String params, final List<String> payloads, final TCPClient.Options options);
        void onSayData(final String methodId, final String params);
    }

    public ProtocolModel() {
        client = new TCPClient();
    }

    public void init(final String host, final int port, final ProtocolListener listener) {
        if (protocolThread != null && protocolThread.isAlive()) return;

        protocolThread = new Thread(() -> {
            client.initiate(host, port, new TCPClient.ClientEventsListener() {
                @Override
                public void onClientConnect(TCPClient.Options options) {
                    listener.onHostStatusUpdate(options, HostStatusUpdateMode.Connected);

                    callbacks.get(obj -> {
                        while (!obj.isEmpty())
                            obj.poll().run();

                        return null;
                    });
                }

                @Override
                public void onServerMessage(String head, TCPClient.Options options) {
                    Matcher matcher = requestHeadPattern.matcher(head);
                    if (!matcher.matches()) return;

                    final String method = matcher.group("method").toLowerCase();
                    final String requestId = matcher.group("requestId");
                    final String params = matcher.group("params");

                    options.acknowledgeServer();

                    switch (method) {
                        case "get" -> listener.onGetData(requestId, params, options);
                        case "say" -> listener.onSayData(requestId, params);
                        case "post" -> {
                            final List<String> payloads = new ArrayList<>();
                            final StringBuilder payloadBuilder = new StringBuilder();
                            try {
                                while (true) {
                                    String line = options.awaitServerMessageLine();
                                    if (line == null) return;

                                    matcher = payloadEndPattern.matcher(line);
                                    if (matcher.matches()) break;

                                    matcher = payloadContentHeadPattern.matcher(line);
                                    if (!matcher.matches()) {
                                        options.getSocketWriteStream().println(standardError("Expected payload head"));
                                        return;
                                    }

                                    int charsLeft = Math.abs(Integer.parseInt(matcher.group("length")));
                                    while (charsLeft > 0) {
                                        line = options.awaitServerMessageLine();
                                        if (line == null) return;

                                        payloadBuilder.append(line);
                                        charsLeft -= line.length();
                                    }

                                    if (payloadBuilder.length() > 0) {
                                        payloads.add(payloadBuilder.toString());
                                        payloadBuilder.delete(0, payloadBuilder.length());
                                    }
                                }

                                listener.onPostData(requestId, params, payloads, options);

                                options.getSocketWriteStream().println("DONE");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onClientError(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onClientDisconnect() {
                    listener.onHostStatusUpdate(null, HostStatusUpdateMode.Disconnected);
                }
            });

            protocolThread = null;
        });
        protocolThread.start();
    }

    public void invokeLater(final Runnable runnable) {
        if (protocolThread != null && protocolThread.isAlive())
            runnable.run();
        else
            callbacks.get(obj -> obj.add(runnable));
    }

    public static String standardError(final String message) {
        return "ERR \"" + message + "\"";
    }
}
