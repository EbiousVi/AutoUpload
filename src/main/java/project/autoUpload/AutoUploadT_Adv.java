package project.autoUpload;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import project.filesWalker.FilesWalker;
import project.filesWalker.FilesWalkerT_Adv;
import project.filesWalker.Parts;
import project.props.Props;
import project.props.enums.PropsKeys;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static project.colors.Colors.*;

public class AutoUploadT_Adv extends AutoUploadT {
    private final FilesWalkerT_Adv walkerT_C;

    public AutoUploadT_Adv(FilesWalker walker, FilesWalkerT_Adv walkerT_C) {
        super(walker);
        this.walkerT_C = walkerT_C;
    }

    @Override
    public void start(WebDriver driver) {
        try {
            closeAlert(driver);
            if (walker.parts.get(Parts.QUAL)) executeQualPart(driver);
            if (walker.parts.get(Parts.TECH)) executeTechPart(driver);
            if (walker.parts.get(Parts.COMM)) executeCommPart(driver);
            executeContractsPart(driver);
            printResult();
        } catch (InterruptedException e) {
            printInterruptedException(e);
        }
    }

    private void executeContractsPart(WebDriver driver) throws InterruptedException {
        clearDownloadDir(Props.props.get(PropsKeys.DOWNLOAD_DIR_PATH));
        WebElement contractSection = driver.findElement(
                By.xpath("//*[text() = 'Заключение договора']/ancestor::a"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", contractSection);
        TimeUnit.MILLISECONDS.sleep(1000L);
        List<WebElement> contractsToDownload = driver.findElements(
                By.xpath("//*[contains(text(), 'Сторона по договору (Покупатель):')]/ancestor::fieldset//a[contains(@href,'file')]"));
        TimeUnit.MILLISECONDS.sleep(1000L);
        for (WebElement contract : contractsToDownload) {
            WebDriverWait wait = new WebDriverWait(driver, 60L, 2000L);
            wait.until(ExpectedConditions.elementToBeClickable(contract));
            JavascriptExecutor download = (JavascriptExecutor) driver;
            download.executeScript("arguments[0].click();", contract);
            TimeUnit.MILLISECONDS.sleep(3000L);
            waitDownloading(driver);
        }
        Map<Long, String> downloadedContracts = walkerT_C.collectContracts();
        List<String> contractPaths = new ArrayList<>(downloadedContracts.values());
        List<WebElement> uploadButtons = driver.findElements(
                By.xpath("//button[@style='background-image: url(\"/images/icons/silk/page_add.png\");']"));
        uploadButtons.remove(uploadButtons.size() - 1);
        if (contractPaths.size() == uploadButtons.size()) {
            for (int i = 0; i < uploadButtons.size(); i++) {
                JavascriptExecutor upload = (JavascriptExecutor) driver;
                upload.executeScript("arguments[0].click();", uploadButtons.get(i));
                TimeUnit.MILLISECONDS.sleep(1000L);
                WebElement input = driver.findElement(
                        By.xpath("//*[contains(text(), 'Шаблон')]/ancestor::form//input[@type='file']"));
                input.sendKeys(contractPaths.get(i));
                WebElement uploadFile = driver.findElement(By.xpath("//*[text() = 'Загрузить']"));
                upload.executeScript("arguments[0].click();", uploadFile);
                waiting(driver);
            }
            System.out.println(paintYellow("Работа с договорами окончена!"));
        } else {
            System.out.println(paintRed("Количество скачанных договоров не соответствует числу кнопок для загрузки!"));
            System.out.println(paintRed("Количество договоров = " + contractPaths.size()));
            System.out.println(paintRed("Количество кнопок на сайте не считая последней = " + uploadButtons.size()));
            System.out.println(paintRed("Проверьте эти параметры в ручную!"));
        }
    }

    private void waitDownloading(WebDriver driver) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(90))
                .pollingEvery(Duration.ofMillis(3000L));
        wait.until(waiting -> isDownloaded());
    }

    private boolean isDownloaded() {
        String downloadDirPath = Props.props.get(PropsKeys.DOWNLOAD_DIR_PATH);
        File[] files = (new File(downloadDirPath)).listFiles();
        if (files == null)
            throw new RuntimeException(paintRed("Незапланированная ошибка! Ошибка в пути к папке загрузки Chrome"));
        for (File file : files) {
            if (file.getName().contains("crdownload")) {
                return false;
            }
        }
        return true;
    }

    private void clearDownloadDir(String dirPath) {
        File[] files = (new File(dirPath)).listFiles();
        if (files == null)
            throw new RuntimeException(paintRed("Незапланированная ошибка! Ошибка в пути к папке загрузки Chrome"));
        try {
            for (File file : files) {
                if (file.isFile())
                    FileUtils.forceDelete(file);
                if (file.isDirectory())
                    FileUtils.deleteDirectory(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(paintRed("Не удалось удалить файл/папку, в папке куда браузер сохраняет файлы"), e);
        }
    }
}
