package project.props.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static project.colors.Colors.paintRed;

public enum PropsKeys {
    CHROME_DRIVER_PATH("chrome.driver.path"),
    CHROME_BINARY_PATH("chrome.binary.path"),
    USER_DATA_DIR_PATH("user.data.dir.path"),
    UPLOAD_DIR_PATH("upload.dir.path"),
    DOWNLOAD_DIR_PATH("download.dir.path"),
    PROFILE_DIR_NAME("profile.dir.name"),
    TIMEOUT_LONG("timeout.long"),
    URL("url");

    public final String name;
    public static List<String> required;
    public static List<String> pathKeys = Arrays.asList(
            CHROME_DRIVER_PATH.name, CHROME_BINARY_PATH.name,
            USER_DATA_DIR_PATH.name, UPLOAD_DIR_PATH.name, DOWNLOAD_DIR_PATH.name
    );
    public static List<String> noneKeys = Arrays.asList(URL.name, PROFILE_DIR_NAME.name);
    public static List<String> longKeys = Collections.singletonList(TIMEOUT_LONG.name);

    public static PropsKeys getEnumByValue(String value) {
        PropsKeys[] values = PropsKeys.values();
        for (PropsKeys propsKeys : values) {
            if (propsKeys.name.equals(value)) {
                return propsKeys;
            }
        }
        throw new IllegalArgumentException(paintRed("Не существует Енама по переданному значению!"));
    }

    public static List<String> getRequiredKeys() {
        if (required != null) return required;
        required = Arrays.stream(PropsKeys.values()).map(value -> value.name).collect(Collectors.toList());
        return required;
    }

    PropsKeys(String name) {
        this.name = name;
    }
}
