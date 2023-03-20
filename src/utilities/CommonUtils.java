package utilities;

import java.util.regex.Pattern;

public class CommonUtils {
    public static String getFileExtension(final String fileName) {
        final String[] split = fileName.split(Pattern.quote("."));
        if (split.length == 0)
            return "";

        return split[split.length - 1];
    }
}
