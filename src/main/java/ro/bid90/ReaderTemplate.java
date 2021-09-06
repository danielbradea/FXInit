package ro.bid90;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReaderTemplate {
    String appName;
    String groupId;
    String artefactId;
    String version;
    String versionFX;
    String description;

    public ReaderTemplate(String appName,
                          String groupId,
                          String artefactId,
                          String description,
                          String version,
                          String versionFX) {
        this.appName = appName;
        this.groupId = groupId;
        this.artefactId = artefactId;
        this.version = version;
        this.versionFX = versionFX;
        this.description = description;
    }

    public List<String> readLine(Path path) throws IOException {
        Stream<String> stringList = Files.lines(path);
        return stringList.map(s -> {
            String line = s.replace("-GROUP-ID-", groupId)
                    .replace("-GROUP-TARGET-", groupId.replace(".","/"))
                    .replace("-VERSION-", version)
                    .replace("-APP-", appName)
                    .replace("-FX-VERSION", versionFX)
                    .replace("-DESCRIPTION-", description);
            return line+"\n";
        }).collect(Collectors.toList());

    }
}
