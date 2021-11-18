package project.autoUpload;

import org.openqa.selenium.WebDriver;
import project.filesWalker.FilesWalker;
import project.filesWalker.Parts;

public class AutoUploadT_Base extends AutoUploadT {

    public AutoUploadT_Base(FilesWalker walker) {
        super(walker);
    }

    @Override
    public void start(WebDriver driver) {
        try {
            closeAlert(driver);
            if (walker.parts.get(Parts.QUAL)) executeQualPart(driver);
            if (walker.parts.get(Parts.TECH)) executeTechPart(driver);
            if (walker.parts.get(Parts.COMM)) executeCommPart(driver);
            printResult();
        } catch (InterruptedException e) {
            printInterruptedException(e);
        }
    }
}
