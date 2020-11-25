package fr.gendarmerie.stsisi.audit.tools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    private static Tools INSTANCE;
    private final String date;

    private Tools() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date oDate = new Date();
        date = dateFormat.format(oDate);
    }

    public static Tools getInstance() {
        synchronized (Tools.class) {
            if (INSTANCE == null) {
                INSTANCE = new Tools();
            }
        }
        return INSTANCE;
    }

    public void controlFile(int count, String error, Path file) {
        String stringCurrentPath = pathLog() + "\\" + date + ".txt";
        File logFile = new File(stringCurrentPath);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            Files.write(Paths.get(stringCurrentPath), ("Match(s) sur " + count + " ligne(s) pour => " + error + " sur => " + file+ "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("erreur: " + e);
        }
    }

    public String pathLog() {
        return System.getProperty("user.dir");
    }
}