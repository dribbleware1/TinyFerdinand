package Engine.FileIO;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    private static final String ROOT_DIRECTORY = FileSystems.getDefault().getPath("").toAbsolutePath().toString();

    private static String getJarPath(final String ... names) {
        StringBuilder builder = new StringBuilder();
        builder.append("/");
        for(int i=0; i<names.length-1; ++i) {
            builder.append(names[i]);
            builder.append("/");
        }
        builder.append(names[names.length-1]);
        return builder.toString();
    }

    /**
     * Get a {@link URL} from the given path with the assumption that it ends in a JAR file.
     * @param names list of file paths with the last one being a file and the rest are directories
     * @return URL pointing to the file in JAR
     */
    public static URL getURLFromJar(final String ... names) {
        return FileUtils.class.getResource(getJarPath(names));
    }

    /**
     * Get a {@link InputStream} from the given path with the assumption that it ends in a JAR file.
     * @param names list of file paths with the last one being a file and the rest are directories
     * @return InputStream on the file in JAR
     */
    public static InputStream getResourceAsStreamFromJar(final String ... names) {
        return FileUtils.class.getResourceAsStream(getJarPath(names));
    }

    /**
     * Get a {@link Path} object based on input. note this assumes the last entry in name is a file, not a directory.
     * In addition, this method does not return a valid path to a JAR file. In order to get a correct path into JAR,
     * refer to
     * @param names list of file paths with the last one being a file and the rest are directories
     * @return Path object representing the given path. This is system independent.
     */
    public static Path getFilePathFromRoot(final String ... names) {
        return getFilePath(ROOT_DIRECTORY, names);
    }

    /**
     * Similar to {@method getFilePathFromRoot}, this method returns a {@link Path} object based on input. However,
     * this method does not assume the root is always the location of this engine and instead take it from an input.
     * @param root the root of the file path.
     * @param names the remainder of the file path with the last one being the file and the rest being a directory.
     * @return System independent path object representing the given path.
     */
    public static Path getFilePath(final String root, final String ... names) {
        return Paths.get(root, names);
    }

}
