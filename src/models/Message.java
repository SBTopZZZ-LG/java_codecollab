package models;

import utilities.TCPClient;

public class Message {
    public enum Method {
        GET,
        POST
    }

    public enum RequestId {
        GP_NAME("name"),
        G_CLIENTS("clients"),
        G_LIST("list"),
        GP_BINARY("binary"),
        P_UPDATED_BINARY("updatedBinary"),
        G_MD5("md5"),
        P_CHAT_MSG("chatMsg"),
        P_VERIFY_MD5("verifyMd5");

        public final String label;
        RequestId(final String label) {
            this.label = label;
        }
    }

    public interface MessageCallbackListener {
        void onServerAcknowledge(final String head, final TCPClient.Options options) throws Exception;
        void onError(final Exception e);
    }

    public Method method;
    public String requestId, params;
    public MessageCallbackListener listener;

    public Message(final Method method, final RequestId requestId, final String params, final MessageCallbackListener listener) {
        this.method = method;
        this.requestId = requestId.label;
        this.params = params;
        this.listener = listener;
    }

    @Override
    public String toString() {
        return method.name() + " " + requestId + (params == null ? "" : (" \"" + params + "\""));
    }
}
