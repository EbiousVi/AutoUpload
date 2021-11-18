package project.autoUpload;

import org.openqa.selenium.WebDriver;

import project.filesWalker.FilesWalker;
import project.filesWalker.Parts;

public class AutoUploadG_Adv extends AutoUploadG {

    private static final String techXpath = "//*[contains(text(), 'Первая часть заявки')]";
    private static final String qualXpath = "//*[contains(text(), 'Вторая часть заявки')]";
    private static final String commXpath = "//*[contains(text(), 'Ценовое предложение')]";

    public AutoUploadG_Adv(FilesWalker filesWalker) {
        super(filesWalker);
    }

    @Override
    public void start(WebDriver driver) {
        try {
            if (walker.parts.get(Parts.TECH)) {
                executeTechPart(driver);
            }
            if (walker.parts.get(Parts.QUAL)) {
                clickToTransition(driver, qualXpath);
                executeQualPart(driver);
            }
            if (walker.parts.get(Parts.COMM)) {
                clickToTransition(driver, commXpath);
                executeCommPart(driver);
            }
            printResult();
        } catch (InterruptedException e) {
            printInterruptedException(e);
        }
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
        uploadCommFile(driver);
        countUploadedCommFiles(driver);
    }
}
