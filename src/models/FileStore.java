package models;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileStore {
    public static FileStore instance = null;

    public final String path;

    public FileStore() throws IOException {
        path = String.valueOf(System.currentTimeMillis());
        Files.createDirectory(Path.of(path));
    }

    public String getHash(final String path) throws IOException, NoSuchAlgorithmException {
        if (!exists(path)) return null;

        byte[] data = Files.readAllBytes(Paths.get(this.path + path));
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        return new BigInteger(1, hash).toString(16);
    }

    public void setContent(final String path, final String content) throws IOException {
        final BufferedOutputStream bos = touchFile(path);
        bos.write(content.getBytes(StandardCharsets.UTF_8));
        bos.close();
    }
    public String getContent(final String path) throws IOException {
        if (!exists(path)) return null;

        return Files.readString(Path.of(this.path + path));
    }

    public BufferedInputStream openFile(final String path) throws IOException {
        if (!exists(path)) {
            final File file = new File(this.path + path);
            assert file.exists() || file.createNewFile();
        }

        return new BufferedInputStream(new FileInputStream(this.path + path));
    }
    public BufferedOutputStream touchFile(final String path) throws IOException {
        final File file = new File(this.path + path);
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();

        assert file.exists() || file.createNewFile();

        return new BufferedOutputStream(new FileOutputStream(file));
    }

    public void unlink(final String path) throws IOException {
        if (!exists(path)) return;

        final Path pathObj = Path.of(this.path + path);
        if (Files.isDirectory(pathObj))
            deleteDirectoryRecursively(pathObj.toFile());
        else
            Files.delete(pathObj);
    }
    protected void deleteDirectoryRecursively(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryRecursively(file);
            }
        }

        //noinspection ResultOfMethodCallIgnored
        directoryToBeDeleted.delete();
    }

    public boolean exists(final String path) {
        return Files.exists(Path.of(this.path + path));
    }

    public String resolvePath(final String path) {
        return this.path + path;
    }
}
