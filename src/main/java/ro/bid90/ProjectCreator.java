package ro.bid90;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class ProjectCreator implements Runnable {
    String appName;
    String groupId;
    String artefactId;
    String version;
    String versionFX;
    ProjectCreatorProgress progress;
    Path path;
    double linesSize = 0;
    double lineWriteNr = 0;

    public ProjectCreator(String appName, String groupId, String artefactId, String version, String versionFX, Path path, ProjectCreatorProgress progress) {
        this.appName = appName;
        this.groupId = groupId;
        this.artefactId = artefactId;
        this.version = version;
        this.versionFX = versionFX;
        this.progress = progress;
        this.path = path;
    }

    @Override
    public void run() {

        Path rootFolder = path.resolve(appName);
        Path resourcesPath;
        Path packagePath;
        Path javaPath;
        Path controllerPath;
        List<String> appTemplate = null;
        List<String> mainTemplate = null;
        List<String> mainControllerTemplate = null;
        List<String> moduleTemplate = null;
        List<String> pomTemplate = null;


        resourcesPath = rootFolder.resolve("src").resolve("main").resolve("resources");
        javaPath = rootFolder.resolve("src").resolve("main").resolve("java");
        packagePath = javaPath;
        String[] packages = groupId.split("\\.");
        System.out.println(packages.length);
        for (String s : packages){
            packagePath = packagePath.resolve(s);
        }

        controllerPath = packagePath.resolve("controller");
        ReaderTemplate template = new ReaderTemplate(appName, groupId, artefactId, version, versionFX);
        try {
            appTemplate = template.readLine(new File(getClass().getResource("template/App.txt").toURI()).toPath());
            mainTemplate = template.readLine(new File(getClass().getResource("template/main.txt").toURI()).toPath());
            mainControllerTemplate = template.readLine(new File(getClass().getResource("template/MainController.txt").toURI()).toPath());
            moduleTemplate = template.readLine(new File(getClass().getResource("template/module-info.txt").toURI()).toPath());
            pomTemplate = template.readLine(new File(getClass().getResource("template/pom.txt").toURI()).toPath());
            linesSize += appTemplate.size();
            linesSize += mainTemplate.size();
            linesSize += mainControllerTemplate.size();
            linesSize += moduleTemplate.size();
            linesSize += pomTemplate.size();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            createDirectory(rootFolder);
            createDirectory(resourcesPath);
            createDirectory(packagePath);
            createDirectory(controllerPath);
            Files.createFile(rootFolder.resolve("pom.xml"));
            Files.createFile(resourcesPath.resolve("main.fxml"));
            Files.createFile(javaPath.resolve("module-info.java"));
            Files.createFile(packagePath.resolve("App.java"));
            Files.createFile(controllerPath.resolve("MainController.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeFile(rootFolder.resolve("pom.xml"),pomTemplate);
        writeFile(resourcesPath.resolve("main.fxml"),mainTemplate);
        writeFile(javaPath.resolve("module-info.java"),moduleTemplate);
        writeFile(packagePath.resolve("App.java"),appTemplate);
        writeFile(controllerPath.resolve("MainController.java"),mainControllerTemplate);
        
    }

    public interface ProjectCreatorProgress {
        void set(double d);
    }


    private double normalize(double value) {
        return (value - 0) / (this.linesSize - 0);
    }
    private void createDirectory(Path path){
        File dir = new File(path.toString());
        dir.mkdirs();
    }

    private  void writeFile(Path path, List<String> templateLines){
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
