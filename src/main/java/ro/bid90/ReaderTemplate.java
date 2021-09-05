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

    public ReaderTemplate(String appName, String groupId, String artefactId, String version, String versionFX) {
        this.appName = appName;
        this.groupId = groupId;
        this.artefactId = artefactId;
        this.version = version;
        this.versionFX = versionFX;
    }

    public List<String> readLine(Path path) throws IOException {
        Stream<String> stringList = Files.lines(path);
        return stringList.map(s -> {

            if (s.contains("-GROUP-ID-")) {
                String line = s.replace("-GROUP-ID-", groupId);
                return  line+"\n";
            }
            if (s.contains("-APP-")) {
                String line  = s.replace("-APP-", appName);
                return  line+"\n";
            }
            if (s.contains("-FX-VERSION")) {
                String line  = s.replace("-FX-VERSION", versionFX);
                return  line+"\n";
            }
            if (s.contains("-VERSION-")) {
                String line  = s.replace("-VERSION-", version);
                return  line+"\n";
            }
            if(s.contains("-GROUP-TARGET-")){
                String line  = s.replace("-GROUP-TARGET-", groupId.replace(".","/"));
                return  line+"\n";
            }

            return s+"\n";
        }).collect(Collectors.toList());

    }
}
