package utilities;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RequestHandlers {
    public interface PostRequestHandler {
        boolean handler(final String requestId, final String params, final List<String> payloads, final TCPClient.Options options);
    }

    public static boolean postChatMsg(final String requestId, final String params, final List<String> payloads, final TCPClient.Options options) {
        if (!requestId.equalsIgnoreCase("chatMsg")) return false;

        System.out.println("Sender (" + params + "): " + payloads.stream().map(StringEscapeUtils::unescapeJava).collect(Collectors.joining()));

        return true;
    }
}
