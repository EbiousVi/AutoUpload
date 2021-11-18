package project.filesWalker;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static project.colors.Colors.paintRed;
import static project.colors.Colors.paintYellow;

public class FilesWalkerG_Adv extends FilesWalker {

    private final List<String> rejectedFiles = new ArrayList<>();

    public FilesWalkerG_Adv(Map<Parts, Boolean> parts) {
        super(parts);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.getFileName().toString().startsWith("~$")) return FileVisitResult.CONTINUE;
        if (parts.get(Parts.TECH) && file.getParent().toString().contains(Parts.TECH.name))
            if (file.getFileName().toString().length() <= 50) {
                techMap.put(file.toAbsolutePath().toString(), file.getFileName().toString().replaceAll("\\.[a-zA-Z].+", ""));
            } else {
                rejectedFiles.add(file.getFileName().toString());
            }
        if (parts.get(Parts.QUAL) && file.getParent().toString().contains(Parts.QUAL.name))
            if (file.getFileName().toString().length() <= 50) {
                qualMap.put(file.toAbsolutePath().toString(), file.getFileName().toString().replaceAll("\\.[a-zA-Z].+", ""));
            } else {
                rejectedFiles.add(file.getFileName().toString());
            }
        if (parts.get(Parts.COMM) && file.getParent().toString().contains(Parts.COMM.name))
            if (file.getFileName().toString().length() <= 50) {
                commMap.put(file.toAbsolutePath().toString(), file.getFileName().toString().replaceAll("\\.[a-zA-Z].+", ""));
            } else {
                rejectedFiles.add(file.getFileName().toString());
            }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public boolean isSuccessWalked(String dirPath) {
        boolean walk = walk(dirPath);
        if (walk) {
            if (hasRejectedFiles()) {
                return false;
            } else {
                return isNotEmptyDirs();
            }
        } else {
            return false;
        }
    }

    public boolean hasRejectedFiles() {
        if (!rejectedFiles.isEmpty()) {
            printOverSizeFileNameInfo();
            rejectedFiles.clear();
            return true;
        }
        return false;
    }

    private void printOverSizeFileNameInfo() {
        for (String str : rejectedFiles) {
            System.out.print(paintYellow(str.substring(0, 50)));
            System.out.println(paintRed("| - Имя файла заканчивается на 50-ом символе!"));
            System.out.println(paintRed("Длина имени должна быть не больше 50 символов, включая расширение (.pdf, .doc, и т.д)"));
        }
    }
}
