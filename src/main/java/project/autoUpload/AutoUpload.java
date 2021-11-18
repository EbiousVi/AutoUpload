package project.autoUpload;

import org.fusesource.jansi.Ansi;
import org.openqa.selenium.WebDriver;
import project.filesWalker.FilesWalker;
import project.filesWalker.Parts;

import static project.colors.Colors.paintRed;

public abstract class AutoUpload {

    int countFilesBeforeUpload;
    int countFilesDuringUpload;
    int countFilesAfterUpload;
    final FilesWalker walker;

    public AutoUpload(FilesWalker walker) {
        this.walker = walker;
    }

    public FilesWalker getWalker() {
        return walker;
    }

    public abstract void start(WebDriver paramWebDriver) throws InterruptedException;

    abstract void executeTechPart(WebDriver driver) throws InterruptedException;

    abstract void executeQualPart(WebDriver driver) throws InterruptedException;

    abstract void executeCommPart(WebDriver driver) throws InterruptedException;

    abstract void countUploadedTechFiles(WebDriver driver);

    abstract void countUploadedQualFiles(WebDriver driver);

    abstract void countUploadedCommFiles(WebDriver driver);

    void printInterruptedException(Exception e) {
        System.out.println(paintRed("Проблемы с работой потоков выполнения!"));
        System.out.println(paintRed(e.toString()));
        System.exit(-1);
    }

    void printResult() {
        if (walker.parts.get(Parts.TECH)) countFilesBeforeUpload += walker.techMap.size();
        if (walker.parts.get(Parts.QUAL)) countFilesBeforeUpload += walker.qualMap.size();
        if (walker.parts.get(Parts.COMM)) countFilesBeforeUpload += walker.commMap.size();
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a(""));
        System.out.println("Перед загрузкой = " + countFilesBeforeUpload);
        System.out.println("Загружено = " + countFilesDuringUpload);
        System.out.println("После загрузки = " + countFilesAfterUpload);
        System.out.println(Ansi.ansi().reset());
    }
}
