package fr.gendarmerie.stsisi.audit.controles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Tools {
    public void controlFile(String stringCurrentPath, String error) {
        File logFile = new File(stringCurrentPath);
        boolean b = false;
        try {
            b = logFile.createNewFile();
            Files.write(Paths.get(stringCurrentPath), (error+"\n").getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("erreur: "+e);
        } finally {
            if (b) {
                System.out.println("Création d'un fichier log.txt => "+stringCurrentPath);
            }
        }
    }
}