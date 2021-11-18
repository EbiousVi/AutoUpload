package project.filesWalker;

import project.props.Props;
import project.props.enums.PropsKeys;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.TreeMap;

import static project.colors.Colors.paintRed;
import static project.colors.Colors.paintYellow;

public class FilesWalkerT_Adv extends FilesWalker {

    private static final Map<Long, String> contracts = new TreeMap<>();

    public FilesWalkerT_Adv(Map<Parts, Boolean> parts) {
        super(parts);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        contracts.put(attrs.lastModifiedTime().toMillis(), file.toAbsolutePath().toString());
        return FileVisitResult.CONTINUE;
    }

    public Map<Long, String> collectContracts() {
        try {
            Path path = Paths.get(Props.props.get(PropsKeys.DOWNLOAD_DIR_PATH));
            Files.walkFileTree(path, this);
            return contracts;
        } catch (Exception e) {
            throw new RuntimeException(paintRed("Ошибка во время сбора файлов, в папке загрузки Договоров!"), e);
        }
    }
}
