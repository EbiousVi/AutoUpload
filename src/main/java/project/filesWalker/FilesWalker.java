package project.filesWalker;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.TreeMap;

import static project.colors.Colors.*;

public abstract class FilesWalker extends SimpleFileVisitor<Path> {

    public final Map<String, String> techMap = new TreeMap<>();
    public final Map<String, String> qualMap = new TreeMap<>();
    public final Map<String, String> commMap = new TreeMap<>();
    public final Map<Parts, Boolean> parts;

    public FilesWalker(Map<Parts, Boolean> parts) {
        this.parts = parts;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.getFileName().toString().startsWith("~$")) return FileVisitResult.CONTINUE;
        if (parts.get(Parts.TECH) && file.getParent().toString().contains(Parts.TECH.name))
            techMap.put(file.toAbsolutePath().toString(), file.getFileName().toString().replaceAll("\\.[a-zA-Z].+", ""));
        if (parts.get(Parts.QUAL) && file.getParent().toString().contains(Parts.QUAL.name))
            qualMap.put(file.toAbsolutePath().toString(), file.getFileName().toString().replaceAll("\\.[a-zA-Z].+", ""));
        if (parts.get(Parts.COMM) && file.getParent().toString().contains(Parts.COMM.name))
            commMap.put(file.toAbsolutePath().toString(), file.getFileName().toString().replaceAll("\\.[a-zA-Z].+", ""));
        return FileVisitResult.CONTINUE;
    }

    public boolean isSuccessWalked(String dirPath) {
        boolean isWalked = walk(dirPath);
        if (isWalked) {
            return isNotEmptyDirs();
        } else {
            return false;
        }
    }

    boolean walk(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!path.isAbsolute()) {
                System.out.println(paintRed("Введен не валидный путь! Путь не соответствует пути операционной системы"));
                return false;
            }
            this.clearPathsStorage();
            Files.walkFileTree(path, this);
            return true;
        } catch (IOException e) {
            System.out.println(paintRed("Ошибка при попытки обхода заданной папки!"));
            System.out.println(paintRed(e.toString()));
        } catch (InvalidPathException e) {
            System.out.println(
                    paintRed("Не допустимые символы в пути к папке! Недопустимые символы <") +
                            paintWhite(e.getReason()) + paintRed(">"));
        }
        return false;
    }

    boolean isNotEmptyDirs() {
        boolean isEmptyDirs = (techMap.isEmpty() && qualMap.isEmpty() && commMap.isEmpty());
        if (isEmptyDirs) {
            System.out.println(paintRed("В папках Техника, Квалификация, Коммерческая отсутствуют файлы" +
                    "\nИли структура папки не соответствует шаблону!"));
            return false;
        } else {
            printFoundedFiles();
            return true;
        }
    }

    void clearPathsStorage() {
        if (qualMap.isEmpty() && techMap.isEmpty() && commMap.isEmpty()) return;
        qualMap.clear();
        techMap.clear();
        commMap.clear();
    }

    private void printFoundedFiles() {
        if (parts.get(Parts.TECH)) {
            printPart("В Технике: ", techMap, "Техника: ");
        }
        if (parts.get(Parts.QUAL)) {
            printPart("В Квалификации: ", qualMap, "Квалификация: ");
        }
        if (parts.get(Parts.COMM)) {
            printPart("В Коммерческой: ", commMap, "Коммерческая: ");
        }
        System.out.println(paintCyan("Всего: " + (techMap.size() + qualMap.size() + commMap.size()) + " файлов"));
    }

    private void printPart(String titlePartName, Map<String, String> map, String bodyPartName) {
        System.out.println(paintCyan(titlePartName + map.size() + " файл"));
        map.forEach((k, v) -> System.out.println(paintYellow(bodyPartName + k)));
    }
}
