package project.autoUpload;

import org.openqa.selenium.WebDriver;
import project.filesWalker.FilesWalker;
import project.filesWalker.Parts;

public class AutoUploadG_Base extends AutoUploadG {

    public AutoUploadG_Base(FilesWalker walker) {
        super(walker);
    }

    @Override
    public void start(WebDriver driver) {
        try {
            if (walker.parts.get(Parts.TECH)) executeTechPart(driver);
            if (walker.parts.get(Parts.QUAL)) executeQualPart(driver);
            if (walker.parts.get(Parts.COMM)) executeCommPart(driver);
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
