package Engine.FileIO;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    private static final String ROOT_DIRECTORY = FileSystems.getDefault().getPath("").toAbsolutePath().toString();

    public static Path getFilePath(final String ... names) {
        return Paths.get(ROOT_DIRECTORY, names);
    }

}
