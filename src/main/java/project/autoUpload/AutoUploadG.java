package project.autoUpload;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import project.filesWalker.FilesWalker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AutoUploadG extends AutoUpload {

    public AutoUploadG(FilesWalker walker) {
        super(walker);
    }

    @Override
    void countUploadedTechFiles(WebDriver driver) {
        List<WebElement> uploadedTechFiles = driver.findElements(
                By.xpath("//*[text() = 'Техническое предложение и иные документы']/ancestor::fieldset//a[contains(@href,'file')]"));
        countFilesAfterUpload += uploadedTechFiles.size();
    }

    @Override
    void countUploadedQualFiles(WebDriver driver) {
        List<WebElement> uploadedQualFiles = driver.findElements(
                By.xpath("//*[text() = 'Иные документы']/ancestor::fieldset//a[contains(@href,'file')]"));
        countFilesAfterUpload += uploadedQualFiles.size();
    }

    @Override
    void countUploadedCommFiles(WebDriver driver) {
        List<WebElement> uploadedCommFiles = driver.findElements(
                By.xpath("//fieldset[@id='price_offer_docs_wrapper_id']//a[contains(@href,'file')]"));
        countFilesAfterUpload += uploadedCommFiles.size();
    }


    void clickToTransition(WebDriver driver, String xPath) throws InterruptedException {
        WebElement clickableElem = driver.findElement(By.xpath(xPath));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", clickableElem);
        TimeUnit.MILLISECONDS.sleep(2000L);
    }

    void uploadTechPart(WebDriver driver) throws InterruptedException {
        for (Map.Entry<String, String> pair : walker.techMap.entrySet()) {
            WebElement techFileName = driver.findElement(
                    By.xpath("//*[text() = 'Техническое предложение и иные документы']/ancestor::fieldset//input[@type='text']"));
            WebElement techFile = driver.findElement(
                    By.xpath("//*[text() = 'Техническое предложение и иные документы']/ancestor::fieldset//input[@type='file'][@class='x-form-file']"));
            techFileName.sendKeys(pair.getValue());
            TimeUnit.MILLISECONDS.sleep(1000L);
            techFile.sendKeys(pair.getKey());
            waiting(driver);
        }
    }

    void uploadQualPart(WebDriver driver) throws InterruptedException {
        for (Map.Entry<String, String> pair : walker.qualMap.entrySet()) {
            WebElement qualFileName = driver.findElement(
                    By.xpath("//*[text() = 'Иные документы']/ancestor::fieldset//input[@type='text']"));
            WebElement qualFile = driver.findElement(
                    By.xpath("//*[text() = 'Иные документы']/ancestor::fieldset//input[@type='file']"));
            qualFileName.sendKeys(pair.getValue());
            TimeUnit.MILLISECONDS.sleep(1000L);
            qualFile.sendKeys(pair.getKey());
            waiting(driver);
        }
    }

    void uploadCommFile(WebDriver driver) throws InterruptedException {
        for (Map.Entry<String, String> pair : walker.commMap.entrySet()) {
            WebElement commFileName = driver.findElement(
                    By.xpath("//fieldset[@id='price_offer_docs_wrapper_id']//input[@type='text']"));
            WebElement commFile = driver.findElement(
                    By.xpath("//fieldset[@id='price_offer_docs_wrapper_id']//input[@type='file'][@class='x-form-file']"));
            commFileName.sendKeys(pair.getValue());
            TimeUnit.MILLISECONDS.sleep(1000L);
            commFile.sendKeys(pair.getKey());
            waiting(driver);
        }
    }

    private void waiting(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 180L, 2000L);
        wait.until(ExpectedConditions.invisibilityOfElementWithText(
                By.xpath("//*[text() = 'Загрузка файла']"), "Загрузка файла"));
        countFilesDuringUpload++;
        TimeUnit.MILLISECONDS.sleep(1000L);
    }
}
