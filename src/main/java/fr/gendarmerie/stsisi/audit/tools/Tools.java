package fr.gendarmerie.stsisi.audit.tools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Tools {
    public void controlFile(String stringCurrentPath, String error) {
        File logFile = new File(stringCurrentPath);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            Files.write(Paths.get(stringCurrentPath), (error + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("erreur: " + e);
        }
    }

    public String pathFile() {
        String path = System.getProperty("user.dir");
//        return "C:\\Users\\Shadow\\IdeaProjects\\audit";
        return path;
    }

    public String addressJson() {
        return "https://reports.exodus-privacy.eu.org/api/trackers";
    }

    public String stringBuilder(String strCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("(.*)(");
        sb.append(strCode);
        sb.append(")(.*)");
        return sb.toString();
    }
}