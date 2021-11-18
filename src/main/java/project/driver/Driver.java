package project.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import project.props.Props;
import project.props.enums.PropsKeys;

public class Driver {
    private static final String chromeDriver = "webdriver.chrome.driver";
    private static final String chromeBinary = "webdriver.chrome.binary";
    private static final String userDataDir = "user-data-dir=";
    private static final String profileDir = "profile-directory=";

    public static WebDriver getChromeWebDriver() {
        System.setProperty(chromeDriver, Props.props.get(PropsKeys.CHROME_DRIVER_PATH));
        System.setProperty(chromeBinary, Props.props.get(PropsKeys.CHROME_BINARY_PATH));
        ChromeOptions options = new ChromeOptions();
        options.addArguments(userDataDir + Props.props.get(PropsKeys.USER_DATA_DIR_PATH));
        options.addArguments(profileDir + Props.props.get(PropsKeys.PROFILE_DIR_NAME));
        return new ChromeDriver(options);
    }
}
