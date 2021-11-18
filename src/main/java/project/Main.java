package project;

import org.fusesource.jansi.AnsiConsole;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import project.autoUpload.AutoUpload;
import project.autoUpload.AutoUploadFactory;
import project.driver.Driver;

import project.filesWalker.FilesWalker;
import project.filesWalker.Parts;
import project.props.Props;
import project.props.enums.PropsKeys;
import project.types.SubTypes;
import project.types.Types;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static project.colors.Colors.*;

public class Main {

    private static String encoding = "UTF-8";

    static {
        AnsiConsole.systemInstall();
        setEncoding();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        PrintStream out = new PrintStream(System.out, true, encoding);
        System.setOut(out);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, encoding))) {
            Types type = chooseType(reader);
            SubTypes subType = chooseSubType(reader, type);
            Map<Parts, Boolean> parts = chooseParts(reader);
            AutoUpload autoUpload = AutoUploadFactory.getAutoUploadInstance(subType, parts);
            prepareBeforeStart(reader, autoUpload.getWalker());
            start(autoUpload);
        } catch (IOException e) {
            printException(e, "Ошибка ввода!");
        }
    }

    private static void setEncoding() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("windows")) {
            encoding = "Cp1251";
        }
    }

    private static Types chooseType(BufferedReader reader) {
        while (true) {
            List<String> typesNames = Types.names();
            System.out.println(paintGreen("Выберите тип " + typesNames));
            String input;
            try {
                input = reader.readLine().trim().toLowerCase();
            } catch (IOException e) {
                printException(e, "Ошибка ввода!");
                continue;
            }
            for (Types type : Types.values()) {
                if (input.equals(type.name)) {
                    return type;
                }
            }
            System.out.println(paintRed("Вы ввели <") + paintWhite(input) + paintRed(">"));
            System.out.println(paintRed("Список доступных значений " + typesNames));
        }
    }

    private static SubTypes chooseSubType(BufferedReader reader, Types type) {
        while (true) {
            List<SubTypes> subTypesByType = Types.getSubTypesByType(type);
            List<String> subTypesNames = subTypesByType.stream().map(subType -> subType.name).collect(Collectors.toList());
            System.out.println(paintGreen("Выберите подтип " + subTypesNames));
            String input;
            try {
                input = reader.readLine().trim().toLowerCase();
            } catch (IOException e) {
                printException(e, "Ошибка ввода!");
                continue;
            }
            for (SubTypes subType : subTypesByType) {
                if (input.equals(subType.name)) {
                    return subType;
                }
            }
            System.out.println(paintRed("Вы ввели <") + paintWhite(input) + paintRed(">"));
            System.out.println(paintRed("Список доступных значений " + subTypesNames));
        }
    }

    private static Map<Parts, Boolean> chooseParts(BufferedReader reader) {
        Map<Parts, Boolean> parts = new HashMap<>();
        if (askYesOrNo(reader, "Загружать файлы во все части? Ответ [да, нет]")) {
            parts.put(Parts.TECH, Boolean.TRUE);
            parts.put(Parts.QUAL, Boolean.TRUE);
            parts.put(Parts.COMM, Boolean.TRUE);
        } else {
            parts.put(Parts.TECH, askYesOrNo(reader, "Загружать файлы в Технику? Ответ [да, нет]"));
            parts.put(Parts.QUAL, askYesOrNo(reader, "Загружать файлы в Квалификацию? Ответ [да, нет]"));
            parts.put(Parts.COMM, askYesOrNo(reader, "Загружать файлы в Коммерческую? Ответ [да, нет]"));
        }
        return parts;
    }

    private static void prepareBeforeStart(BufferedReader reader, FilesWalker walker) {
        while (true) {
            String propsPath = inputPropsPath(reader);
            Props props = new Props();
            boolean isRead = props.readProps(propsPath);
            if (!isRead) continue;
            boolean isSuccessWalked = walker.isSuccessWalked(Props.props.get(PropsKeys.UPLOAD_DIR_PATH));
            if (isSuccessWalked) {
                boolean isConfirm = confirm(reader);
                if (isConfirm) {
                    break;
                }
            }
        }
    }

    private static String inputPropsPath(BufferedReader reader) {
        while (true) {
            System.out.println(paintGreen("Введите путь к загрузочному файлу"));
            String propsPath;
            try {
                propsPath = reader.readLine().trim();
                if (propsPath.length() == 0) continue;
                Path path = Paths.get(propsPath);
                if (!path.isAbsolute()) {
                    System.out.println(paintRed("Введен не корректный путь! Недопустимый для данной операционной системы"));
                    continue;
                }
                if (!Files.exists(path)) {
                    System.out.println(paintRed(paintRed("Файл или каталог не существует!")));
                    continue;
                }
            } catch (InvalidPathException e) {
                System.out.println(
                        paintRed("Не допустимые символы в пути к папке! Недопустимые символы < ") +
                                paintWhite(e.getReason()) +
                                paintRed(" >"));
                continue;
            } catch (IOException e) {
                printException(e, "Ошибка ввода!");
                continue;
            }
            return propsPath;
        }
    }

    private static boolean confirm(BufferedReader reader) {
        while (true) {
            System.out.println(paintGreen("Нажмите Enter для продолжения или введите любые символы для отмены"));
            System.out.println(paintYellow("Браузер Chrome должен быть закрыт!"));
            String input;
            try {
                input = reader.readLine().trim();
            } catch (IOException e) {
                printException(e, "Ошибка ввода!");
                continue;
            }
            return input.length() == 0;
        }
    }

    private static boolean askYesOrNo(BufferedReader reader, String question) {
        while (true) {
            System.out.println(paintGreen(question));
            String input;
            try {
                input = reader.readLine().trim().toLowerCase();
            } catch (IOException e) {
                printException(e, "Ошибка ввода!");
                continue;
            }
            if (input.equals("да")) return true;
            if (input.equals("нет")) return false;
            System.out.println(paintRed("Введите да или нет! Вы ввели <") + paintWhite(input) + paintRed(">"));
        }
    }

    private static void start(AutoUpload autoUpload) {
        try {
            System.out.println(paintCyan("Вывод системной информация о старте подключения к сайту!"));
            WebDriver driver = Driver.getChromeWebDriver();
            driver.get(Props.props.get(PropsKeys.URL));
            TimeUnit.MILLISECONDS.sleep(Long.parseLong(Props.props.get(PropsKeys.TIMEOUT_LONG)));
            System.out.println(paintCyan("Подключение выполнено"));
            System.out.println(paintCyan("Далее любой цвет кроме желтого - является ошибкой!"));
            autoUpload.start(driver);
        } catch (WebDriverException e) {
            e.printStackTrace();
            printWebDriverException(e);
        } catch (InterruptedException e) {
            printException(e, "Внутрення ошибка приложения!");
        }
    }

    private static void printWebDriverException(WebDriverException e) {
        System.out.println();
        System.out.println(paintRed("Ошибка взаимодействия с сайтом!"));
        System.out.println();
        System.out.println(paintRed(e.getMessage()));
        System.exit(-1);
    }

    private static void printException(Exception e, String message) {
        System.out.println(paintRed(message));
        System.out.println(paintRed(e.toString()));
    }
}
