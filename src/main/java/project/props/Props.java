package project.props;

import com.google.common.collect.ImmutableMap;
import project.props.enums.KeysGroup;
import project.props.enums.PropsKeys;
import project.props.validator.LongValidator;
import project.props.validator.NoneValidator;
import project.props.validator.PathValidator;
import project.props.validator.Validator;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static project.colors.Colors.paintRed;

public class Props {

    public static ImmutableMap<PropsKeys, String> props;
    private final List<String> requiredKeys = PropsKeys.getRequiredKeys();
    private static final Map<KeysGroup, Validator> groups = new HashMap<>();

    static {
        groups.put(KeysGroup.PATH, new PathValidator());
        groups.put(KeysGroup.LONG, new LongValidator());
        groups.put(KeysGroup.NONE, new NoneValidator());
    }

    public boolean readProps(String path) {
        List<String> propsLines;
        try {
            propsLines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            printIOException(e);
            return false;
        }
        List<String> linesEscapeBackSlashes = escapingBackSlashesForWindowsOS(propsLines);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : linesEscapeBackSlashes) {
            stringBuilder.append(line).append("\n");
        }
        try (StringReader stringReader = new StringReader(stringBuilder.toString())) {
            Properties properties = new Properties();
            properties.load(stringReader);
            props = collectPropertyMap(properties);
            return !props.isEmpty();
        } catch (IOException e) {
            printIOException(e);
            return false;
        }
    }

    private void printIOException(Exception e) {
        System.out.println(paintRed("Ошибка чтения из загрузочного файла!"));
        System.out.println(paintRed(e.toString()));
    }

    private List<String> escapingBackSlashesForWindowsOS(List<String> lines) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return lines.stream().map(line -> line.replace("\\", "\\\\")).collect(Collectors.toList());
        }
        return lines;
    }

    private ImmutableMap<PropsKeys, String> collectPropertyMap(Properties properties) {
        int countKeys = 0;
        ImmutableMap.Builder<PropsKeys, String> builder = ImmutableMap.builder();
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            boolean isValidKey = validateKey(key);
            if (isValidKey) {
                String value = properties.getProperty(key).trim();
                KeysGroup group = defineKeyGroup(key);
                boolean isValidValue = groups.get(group).validate(key, value);
                if (isValidValue) {
                    builder.put(PropsKeys.getEnumByValue(key), value);
                    countKeys++;
                } else {
                    return ImmutableMap.<PropsKeys, String>builder().build();
                }
            } else {
                return ImmutableMap.<PropsKeys, String>builder().build();
            }
        }
        if (countKeys == requiredKeys.size()) {
            return builder.build();
        } else {
            int diff = requiredKeys.size() - countKeys;
            if (diff == 1) {
                System.out.println(paintRed("Не хватает --> " + diff + " ключа"));
            } else {
                System.out.println(paintRed("Не хватает --> " + diff + " ключей"));
            }
            System.out.println(paintRed("Список необходимых ключей --> " + requiredKeys));
            return ImmutableMap.<PropsKeys, String>builder().build();
        }
    }

    private boolean validateKey(String key) {
        if (!requiredKeys.contains(key)) {
            System.out.println(paintRed("Ошибка в имени ключа --> " + key));
            System.out.println(paintRed("Список необходимых ключей --> " + requiredKeys));
            return false;
        }
        return true;
    }

    private KeysGroup defineKeyGroup(String value) {
        for (KeysGroup group : KeysGroup.values()) {
            if (value.endsWith(group.value)) {
                return group;
            }
        }
        return KeysGroup.NONE;
    }
}
