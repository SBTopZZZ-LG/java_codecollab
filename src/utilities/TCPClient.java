package utilities;

import models.Message;
import models.Synchronized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPClient {
    public Socket socket = null;

    public final Synchronized<Queue<Message>> messageQueue = new Synchronized<>(new LinkedList<>());
    private volatile Message responseToMessage = null;

    public interface Options {
        Socket getServer();
        PrintWriter getSocketWriteStream();
        String awaitServerMessageLine() throws IOException;
        String awaitServerMessageLineWithTimeout(final Synchronized<Boolean> didTimeoutVar) throws IOException;
        void acknowledgeWithError(final String error);
        BufferedReader getSocketReadStream();
        void acknowledgeServer();
    }
    public interface ClientEventsListener {
        void onClientConnect(final Options options);
        void onServerMessage(final String head, final Options options) throws IOException;
        void onClientError(final Exception e);
        void onClientDisconnect();
    }

    public void initiate(final String host, final int port, final ClientEventsListener listener) {
        if (socket != null)
            return;

        try {
            socket = new Socket(host, port);

            final PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            final Options options = new Options() {
                @Override
                public Socket getServer() {
                    return socket;
                }

                @Override
                public PrintWriter getSocketWriteStream() {
                    return writer;
                }

                @Override
                public String awaitServerMessageLine() throws IOException {
                    //noinspection StatementWithEmptyBody
                    while (!reader.ready());

                    return reader.readLine();
                }

                @Override
                public String awaitServerMessageLineWithTimeout(Synchronized<Boolean> didTimeoutVar) throws IOException {
                    while (!reader.ready()) {
                        final AtomicBoolean shouldBreak = new AtomicBoolean(false);
                        didTimeoutVar.get(didTimeout -> {
                            shouldBreak.set(didTimeout);
                            return null;
                        });

                        if (shouldBreak.get())
                            break;
                    }

                    return reader.readLine();
                }

                @Override
                public void acknowledgeWithError(String error) {
                    writer.println("Error: " + error);
                }

                @Override
                public BufferedReader getSocketReadStream() {
                    return reader;
                }

                @Override
                public void acknowledgeServer() {
                    getSocketWriteStream().println("OK");
                }
            };
            listener.onClientConnect(options);

            String line;
            while (socket.isConnected()) {
                while (!reader.ready()) {
                    if (responseToMessage != null) continue;

                    messageQueue.get(obj -> {
                        final Message message = obj.poll();
                        if (message == null) return null;

                        writer.println(message);
                        responseToMessage = message;

                        return null;
                    });
                }
                if ((line = reader.readLine()) == null) continue;

                if (responseToMessage != null) {
                    try {
                        responseToMessage.listener.onServerAcknowledge(line, options);
                    } catch (Exception e) {
                        responseToMessage.listener.onError(e);
                    } finally {
                        responseToMessage = null;
                    }
                } else {
                    try {
                        listener.onServerMessage(line, options);
                    } catch (IOException e) {
                        listener.onClientError(e);
                    }
                }
            }
        } catch (IOException e) {
            listener.onClientError(e);
            return;
        }

        listener.onClientDisconnect();
    }
}
