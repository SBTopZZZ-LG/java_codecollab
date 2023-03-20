package models;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.Arrays;

public class FileWrapper {
    public enum FileStatus {
        IDLE,
        SAVING,
        DOWNLOADING
    }

    public FileStatus fileStatus = FileStatus.IDLE;
    public String name, absolutePath;
    public boolean isFile;

    public FileWrapper(final String name, final String absolutePath, final boolean isFile) {
        this.name = name;
        this.absolutePath = absolutePath;
        this.isFile = isFile;
    }

    public static void sort(final FileWrapper[] wrappers) {
        Arrays.sort(wrappers, (wrapper1, wrapper2) -> {
            if (wrapper1.isFile != wrapper2.isFile)
                return (wrapper1.isFile ? 1 : 0) - (wrapper2.isFile ? 1 : 0);
            return wrapper1.name.compareToIgnoreCase(wrapper2.name);
        });
    }
}
