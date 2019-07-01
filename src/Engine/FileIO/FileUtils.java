package Engine.FileIO;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    private static final String ROOT_DIRECTORY = FileSystems.getDefault().getPath("").toAbsolutePath().toString();

    /**
     * Get a {@link Path} object based on input. note this assumes the last
     * entry in name is a file, not a directory.
     *
     * @param names list of file paths with the last one being a file and the
     * rest are directories
     * @return Path object representing the given path. This is system
     * independent.
     */
    public static Path getFilePathFromRoot(final String... names) {
        return getFilePath(ROOT_DIRECTORY, names);
    }

    /**
     * Similar to {
     *
     * @method getFilePathFromRoot}, this method returns a {@link Path} object
     * based on input. However, this method does not assume the root is always
     * the location of this engine and instead take it from an input.
     * @param root the root of the file path.
     * @param names the remainder of the file path with the last one being the
     * file and the rest being a directory.
     * @return System independent path object representing the given path.
     */
    public static Path getFilePath(final String root, final String... names) {
        return Paths.get(root, names);
    }

}
