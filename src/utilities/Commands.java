package utilities;

import models.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    public static final long COMMAND_TIMEOUT = 20000;

    public static CompletableFuture<Response<FileWrapper[]>> getList(final String path) {
        final Pattern itemPattern = Pattern.compile("^\"(?<itemName>.+)\" *\\((?<itemType>file|folder)\\)$", Pattern.CASE_INSENSITIVE);

        final CompletableFuture<Response<FileWrapper[]>> future = new CompletableFuture<>();
        ProtocolModel.instance.client.messageQueue.get(instance -> {
            instance.add(new Message(Message.Method.GET, Message.RequestId.G_LIST, path, new Message.MessageCallbackListener() {
                @Override
                public void onServerAcknowledge(String head, TCPClient.Options options) {
                    final Boolean success = Timeout.withCallable(didTimeout -> {
                        final String root = options.awaitServerMessageLineWithTimeout(didTimeout);
                        if (root == null) return false;

                        System.out.println("Root: " + root);

                        String line = options.awaitServerMessageLineWithTimeout(didTimeout);
                        if (line == null) return false;
                        final int count = Integer.parseInt(line);

                        final FileWrapper[] wrappers = new FileWrapper[count + 1];
                        for (int i = 0; i < count; i++) {
                            line = options.awaitServerMessageLineWithTimeout(didTimeout);
                            if (line == null) return false;

                            final Matcher matcher = itemPattern.matcher(line);
                            if (!matcher.matches()) return true;
                            final String itemName = matcher.group("itemName");
                            final String itemType = matcher.group("itemType");

                            wrappers[i + 1] = new FileWrapper(itemName, path + (path.endsWith("/") ? "" : "/") + itemName, itemType.equalsIgnoreCase("file"));
                        }
                        wrappers[0] = new FileWrapper(root, path, false);

                        if (options.awaitServerMessageLineWithTimeout(didTimeout) == null) return false;

                        future.complete(Response.withSuccess(wrappers));

                        return true;
                    }, COMMAND_TIMEOUT);

                    if (Boolean.FALSE.equals(success))
                        future.complete(null);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

            }));

            return null;
        });

        return future;
    }

    public static CompletableFuture<Response<Void>> getBinary(final String path, final BufferedOutputStream bos) {
        final CompletableFuture<Response<Void>> future = new CompletableFuture<>();
        ProtocolModel.instance.client.messageQueue.get(instance -> {
            instance.add(new Message(Message.Method.GET, Message.RequestId.GP_BINARY, path, new Message.MessageCallbackListener() {
                @Override
                public void onServerAcknowledge(String head, TCPClient.Options options) {
                    final Boolean success = Timeout.withCallable(didTimeout -> {
                        String line;
                        while (true) {
                            if ((line = options.awaitServerMessageLineWithTimeout(didTimeout)) == null) return false;
                            if (line.equalsIgnoreCase("DONE")) break;

                            bos.write(StringEscapeUtils.unescapeJava(line).getBytes(StandardCharsets.UTF_8));
                        }
                        bos.flush();

                        future.complete(Response.withSuccess(null));

                        return true;
                    }, COMMAND_TIMEOUT);

                    if (Boolean.FALSE.equals(success))
                        future.complete(Response.withFailure());
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

            }));

            return null;
        });

        return future;
    }

    public static CompletableFuture<Response<Boolean>> postVerifyMd5(final String path, final String localMd5) {
        final CompletableFuture<Response<Boolean>> future = new CompletableFuture<>();
        ProtocolModel.instance.client.messageQueue.get(instance -> instance.add(new Message(Message.Method.POST, Message.RequestId.P_VERIFY_MD5, path, new Message.MessageCallbackListener() {
            @Override
            public void onServerAcknowledge(String head, TCPClient.Options options) {
                final Boolean success = Timeout.withCallable(didTimeout -> {
                    options.getSocketWriteStream().println(localMd5);
                    options.getSocketWriteStream().println("DONE");

                    final String line = options.awaitServerMessageLineWithTimeout(didTimeout);
                    if (line == null) {
                        future.complete(Response.withFailure());
                        return false;
                    }

                    if (line.equalsIgnoreCase("false"))
                        future.complete(Response.withSuccess(false));
                    else
                        future.complete(Response.withSuccess(true));

                    return true;
                }, COMMAND_TIMEOUT);

                if (Boolean.FALSE.equals(success))
                    future.complete(null);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

        })));

        return future;
    }

    public static CompletableFuture<Response<Void>> postBinary(final String path, final BufferedInputStream bis) {
        final CompletableFuture<Response<Void>> future = new CompletableFuture<>();
        ProtocolModel.instance.client.messageQueue.get(instance -> {
            instance.add(new Message(Message.Method.POST, Message.RequestId.GP_BINARY, path, new Message.MessageCallbackListener() {
                @Override
                public void onServerAcknowledge(String head, TCPClient.Options options) {
                    try {
                        while (bis.available() > 0)
                            options.getSocketWriteStream().println(StringEscapeUtils.escapeJava(new String(bis.readNBytes(bis.available()))));
                    } catch (IOException e) {
                        options.acknowledgeWithError("Internal error");
                        return;
                    }

                    options.getSocketWriteStream().println("DONE");

                    future.complete(Response.withSuccess(null));
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

            }));

            return null;
        });

        return future;
    }

    public static CompletableFuture<Response<String>> postUpdatedBinary(final String localModified, final String path, final BufferedInputStream bis, final String localMd5) {
        final CompletableFuture<Response<String>> future = new CompletableFuture<>();
        ProtocolModel.instance.client.messageQueue.get(instance -> {
            instance.add(new Message(Message.Method.POST, Message.RequestId.P_UPDATED_BINARY, path, new Message.MessageCallbackListener() {
                @Override
                public void onServerAcknowledge(String head, TCPClient.Options options) {
                    final Boolean success = Timeout.withCallable(didTimeout -> {
                        options.getSocketWriteStream().println(localMd5);

                        options.getSocketWriteStream().println(StringEscapeUtils.escapeJava(localModified));
                        options.getSocketWriteStream().println("-----split-----");
                        while (bis.available() > 0)
                            options.getSocketWriteStream().println(StringEscapeUtils.escapeJava(new String(bis.readNBytes(bis.available()))));

                        options.getSocketWriteStream().println("DONE");

                        final String message = options.awaitServerMessageLineWithTimeout(didTimeout);
                        if (message == null) {
                            future.complete(Response.withFailure());
                            return false;
                        }
                        if (message.equalsIgnoreCase("OK")) {
                            future.complete(Response.withSuccess(null));
                            return false;
                        }

                        String line = "";
                        final AtomicReference<String> merged = new AtomicReference<>("");
                        while (true) {
                            if ((line = options.awaitServerMessageLineWithTimeout(didTimeout)) == null) {
                                future.complete(Response.withFailure());
                                return false;
                            }
                            if (line.equalsIgnoreCase("DONE"))
                                break;

                            merged.set(merged.get() + StringEscapeUtils.unescapeJava(line));
                        }

                        future.complete(Response.withSuccess(merged.get()));

                        return true;
                    }, COMMAND_TIMEOUT);

                    if (Boolean.FALSE.equals(success))
                        future.complete(null);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

            }));

            return null;
        });

        return future;
    }
}
