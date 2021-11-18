package project.autoUpload;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import project.filesWalker.FilesWalker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AutoUploadT extends AutoUpload {

    public AutoUploadT(FilesWalker walker) {
        super(walker);
    }

    @Override
    void executeTechPart(WebDriver driver) throws InterruptedException {
        uploadTechPart(driver);
        countUploadedTechFiles(driver);
    }

    @Override
    void executeQualPart(WebDriver driver) throws InterruptedException {
        uploadQualPart(driver);
        countUploadedQualFiles(driver);
    }

    @Override
    void executeCommPart(WebDriver driver) throws InterruptedException {
        uploadCommPart(driver);
        countUploadedCommFiles(driver);
    }

    @Override
    void countUploadedTechFiles(WebDriver driver) {
        List<WebElement> uploadedTechFiles = driver.findElements(
                By.xpath("//*[contains(text(), 'Техническая документация')]/ancestor::fieldset//a[contains(@href,'file')]"));
        countFilesAfterUpload += uploadedTechFiles.size();
    }

    @Override
    void countUploadedQualFiles(WebDriver driver) {
        List<WebElement> uploadedQualFiles = driver.findElements(
                By.xpath("//*[contains(text(), 'Квалификационная документация')]/ancestor::fieldset//a[contains(@href,'file')]"));
        countFilesAfterUpload += uploadedQualFiles.size();
    }

    @Override
    void countUploadedCommFiles(WebDriver driver) {
        List<WebElement> uploadedCommFilesV1 = driver.findElements(
                By.xpath("//*[contains(text(), 'Дополнительные документы')]/ancestor::fieldset//a[contains(@href,'file')]"));
        if (uploadedCommFilesV1.size() == 0) {
            List<WebElement> uploadedCommFilesV2 = driver.findElements(
                    By.xpath("//*[contains(text(), 'Коммерческое предложение и иные документы')]/ancestor::fieldset//a[contains(@href,'file')]"));
            countFilesAfterUpload += uploadedCommFilesV2.size();
        } else {
            countFilesAfterUpload += uploadedCommFilesV1.size();
        }
    }

    void waiting(WebDriver driver) throws InterruptedException {
        WebDriverWait uploadWait = new WebDriverWait(driver, 180L, 2000L);
        uploadWait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//*[text() = 'Загрузка файла']"), "Загрузка файла"));
        WebDriverWait processingWait = new WebDriverWait(driver, 180L, 2000L);
        processingWait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//*[text() = 'Пожалуйста подождите']"), "Пожалуйста подождите"));
        TimeUnit.MILLISECONDS.sleep(2000L);
    }

    void closeAlert(WebDriver driver) throws InterruptedException {
        WebElement alertWindow = driver.findElement(By.xpath("//*[text() = 'Закрыть']"));
        JavascriptExecutor closeAlertWindow = (JavascriptExecutor) driver;
        closeAlertWindow.executeScript("arguments[0].click();", alertWindow);
        TimeUnit.MILLISECONDS.sleep(2000L);
    }

    private void uploadCommPart(WebDriver driver) throws InterruptedException {
        WebElement commSection = driver.findElement(
                By.xpath("//*[text() = 'Коммерческая часть предложения']/ancestor::a"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", commSection);
        TimeUnit.MILLISECONDS.sleep(1000L);
        for (String s : walker.commMap.keySet()) {
            WebElement commFile;
            try {
                commFile = driver.findElement(
                        By.xpath("//*[contains(text(), 'Дополнительные документы')]/ancestor::fieldset//input[@type='file']"));
            } catch (Exception e) {
                commFile = driver.findElement(
                        By.xpath("//*[contains(text(), 'Коммерческое предложение и иные документы')]/ancestor::fieldset//input[@type='file']"));
            }
            TimeUnit.MILLISECONDS.sleep(1000L);
            commFile.sendKeys(s);
            waiting(driver);
            countFilesDuringUpload++;
        }
    }

    private void uploadTechPart(WebDriver driver) throws InterruptedException {
        WebElement techSection = driver.findElement(
                By.xpath("//*[text() = 'Техническая часть предложения']/ancestor::a"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", techSection);
        TimeUnit.MILLISECONDS.sleep(1000L);
        for (String s : walker.techMap.keySet()) {
            WebElement techFile = driver.findElement(
                    By.xpath("//*[contains(text(), 'Техническая документация')]/ancestor::fieldset//input[@type='file']"));
            TimeUnit.MILLISECONDS.sleep(1000L);
            techFile.sendKeys(s);
            waiting(driver);
            countFilesDuringUpload++;
        }
    }

    private void uploadQualPart(WebDriver driver) throws InterruptedException {
        WebElement qualSection = driver.findElement(
                By.xpath("//*[text() = 'Квалификационная часть предложения']/ancestor::a"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", qualSection);
        TimeUnit.MILLISECONDS.sleep(1000L);
        for (String s : walker.qualMap.keySet()) {
            WebElement qualFile = driver.findElement(
                    By.xpath("//*[contains(text(), 'Квалификационная документация')]/ancestor::fieldset//input[@type='file']"));
            TimeUnit.MILLISECONDS.sleep(1000L);
            qualFile.sendKeys(s);
            waiting(driver);
            countFilesDuringUpload++;
        }
    }
}
/*
    void rightScroll(WebDriver driver) throws InterruptedException {
        WebElement rightArrow = driver.findElement(By.xpath("//div[@class='x-tab-scroller-right x-unselectable']"));
        Actions builder = new Actions(driver);
        builder.moveToElement(rightArrow).click(rightArrow);
        builder.perform();
        TimeUnit.MILLISECONDS.sleep(2000L);
    }
*/