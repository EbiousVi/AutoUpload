package project.props.validator;

import project.props.enums.PropsKeys;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static project.colors.Colors.*;

public class PathValidator implements Validator {

    @Override
    public boolean validate(String key, String value) {
        if (value.length() == 0) {
            System.out.println(paintRed("Пустое значение ключа --> " + key));
            return false;
        }
        if (!PropsKeys.pathKeys.contains(key))
            throw new RuntimeException(paintRed("Ключ не принадлежит своей группе!"));
        try {
            Path path = Paths.get(value);
            if (!path.isAbsolute()) {
                System.out.println(paintRed("Недопустимое значение для ключа --> + key"));
                System.out.println(paintRed("Введен не корректный путь! Недопустимый для данной операционной системы --> " + value));
                return false;
            }
            if (!Files.exists(path)) {
                System.out.println(paintRed("Недопустимое значение для ключа --> " + key));
                System.out.println(paintRed("Файл или каталог не существует! --> " + value));
                return false;
            }
        } catch (InvalidPathException e) {
            System.out.println(
                    paintRed("--> Не допустимые символы в пути к папке! Недопустимые символы < ") +
                            paintWhite(e.getReason()) +
                            paintRed(" >"));
        }
        return true;
    }
}
