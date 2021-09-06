package ro.bid90;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.control.Alert;


public class ProjectCreator implements Runnable {
    String appName;
    String groupId;
    String artefactId;
    String version;
    String description;
    String versionFX;
    ProjectCreatorProgress progress;
    DialogMessage dialogMessage;
    Path path;
    double linesSize = 0;
    double lineWriteNr = 0;

    public ProjectCreator(String appName, String groupId, String artefactId, String version, String description, String versionFX, Path path, ProjectCreatorProgress progress,DialogMessage dialogMessage) {
        this.appName = appName;
        this.groupId = groupId;
        this.artefactId = artefactId;
        this.version = version;
        this.description = description;
        this.versionFX = versionFX;
        this.progress = progress;
        this.dialogMessage = dialogMessage;
        this.path = path;
    }

    @Override
    public void run() {

        Path rootFolder = path.resolve(appName);
        if(Files.isDirectory(rootFolder)){
            dialogMessage.message(Alert.AlertType.ERROR,"There is a folder named "+appName+" in: "+ rootFolder);
            return;
        }
        Path resourcesPath;
        Path packagePath;
        Path javaPath;
        Path controllerPath;
        List<String> mainStageTemplate = null;
        List<String> mainFxTemplate = null;
        List<String> mainControllerTemplate = null;
        List<String> moduleTemplate = null;
        List<String> pomTemplate = null;
        List<String> readmeTemplate = null;
        List<String> gitIgnoreTemplate = null;
        List<String> initAppTemplate = null;


        resourcesPath = rootFolder.resolve("src").resolve("main").resolve("resources");
        javaPath = rootFolder.resolve("src").resolve("main").resolve("java");
        packagePath = javaPath;
        String[] packages = groupId.split("\\.");
        for (String s : packages) {
            packagePath = packagePath.resolve(s);
        }
        controllerPath = packagePath.resolve("controller");
        try {
            createDirectory(rootFolder);
            createDirectory(resourcesPath);
            createDirectory(packagePath);
            createDirectory(controllerPath);
            Files.createFile(rootFolder.resolve("pom.xml"));
            Files.createFile(rootFolder.resolve("readme.md"));
            Files.createFile(rootFolder.resolve(".gitignore"));
            Files.createFile(resourcesPath.resolve("main.fxml"));
            Files.createFile(javaPath.resolve("module-info.java"));
            Files.createFile(packagePath.resolve("MainStage.java"));
            Files.createFile(packagePath.resolve(appName + ".java"));
            Files.createFile(controllerPath.resolve("MainController.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ReaderTemplate template = new ReaderTemplate(appName, groupId, artefactId, description, version, versionFX);
        try {
            mainStageTemplate = template.readLine(new File(getClass().getResource("template/MainStage.txt").toURI()).toPath());
            mainFxTemplate = template.readLine(new File(getClass().getResource("template/main.txt").toURI()).toPath());
            mainControllerTemplate = template.readLine(new File(getClass().getResource("template/MainController.txt").toURI()).toPath());
            moduleTemplate = template.readLine(new File(getClass().getResource("template/module-info.txt").toURI()).toPath());
            pomTemplate = template.readLine(new File(getClass().getResource("template/pom.txt").toURI()).toPath());
            readmeTemplate = template.readLine(new File(getClass().getResource("template/readme.txt").toURI()).toPath());
            gitIgnoreTemplate = template.readLine(new File(getClass().getResource("template/gitignore.txt").toURI()).toPath());
            initAppTemplate = template.readLine(new File(getClass().getResource("template/FXinit.txt").toURI()).toPath());
            linesSize += mainStageTemplate.size();
            linesSize += mainFxTemplate.size();
            linesSize += mainControllerTemplate.size();
            linesSize += moduleTemplate.size();
            linesSize += pomTemplate.size();
            linesSize += readmeTemplate.size();
            linesSize += gitIgnoreTemplate.size();
            linesSize += initAppTemplate.size();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        writeFile(rootFolder.resolve("pom.xml"), pomTemplate);
        writeFile(rootFolder.resolve("readme.md"), readmeTemplate);
        writeFile(rootFolder.resolve(".gitignore"), gitIgnoreTemplate);
        writeFile(resourcesPath.resolve("main.fxml"), mainFxTemplate);
        writeFile(javaPath.resolve("module-info.java"), moduleTemplate);
        writeFile(packagePath.resolve("MainStage.java"), mainStageTemplate);
        writeFile(packagePath.resolve(appName + ".java"), initAppTemplate);
        writeFile(controllerPath.resolve("MainController.java"), mainControllerTemplate);

        dialogMessage.message(Alert.AlertType.INFORMATION, "Done :)");
    }

    public interface ProjectCreatorProgress {
        void set(double d);
    }
    public interface DialogMessage{
        void message(Alert.AlertType type, String message);
    }


    private double normalize(double value) {
        return (value - 0) / (this.linesSize - 0);
    }

    private void createDirectory(Path path) {
        File dir = new File(path.toString());
        dir.mkdirs();
    }

    private void writeFile(Path path, List<String> templateLines) {
        try {
            FileWriter fileWriter = new FileWriter(path.toString());
            templateLines.stream().forEach(s -> {
                lineWriteNr++;
                try {
                    fileWriter.write(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                progress.set(normalize(lineWriteNr));
            });
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
