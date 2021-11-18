package project.filesWalker;

import java.util.Map;

import static project.colors.Colors.paintYellow;

public class FilesWalkerT_Base extends FilesWalker {

    public FilesWalkerT_Base(Map<Parts, Boolean> parts) {
        super(parts);
    }

    @Override
    public boolean isSuccessWalked(String dirPath) {
        boolean specialCase = this.specialCase();
        if (specialCase) return true;
        boolean isWalked = walk(dirPath);
        if (isWalked) {
            return isNotEmptyDirs();
        } else {
            return false;
        }
    }

    private boolean specialCase() {
        int count = 0;
        for (Boolean bool : parts.values()) {
            if (bool.equals(Boolean.FALSE)) count++;
        }
        if (count == parts.size()) {
            System.out.println(paintYellow("Запущен особый случай!"));
            return true;
        } else {
            return false;
        }
    }
}
