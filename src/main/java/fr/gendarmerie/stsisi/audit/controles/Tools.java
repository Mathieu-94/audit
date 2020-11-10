package fr.gendarmerie.stsisi.audit.controles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Tools {
    public void controlFile(String stringCurrentPath, String error) {
        File logFile = new File(stringCurrentPath);
        try {
            logFile.createNewFile();
            Files.write(Paths.get(stringCurrentPath), (error+"\n").getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
