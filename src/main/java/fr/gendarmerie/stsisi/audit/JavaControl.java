package fr.gendarmerie.stsisi.audit;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaControl implements IPlugins {

    @Override
    public boolean controlName(Path f) {
        String name = "[\\w*].java";
        Pattern r = Pattern.compile(name);
        Matcher m = r.matcher(f.toFile().getName());
        //            System.out.println("\nMatch sur " + this.getClass().getCanonicalName() + " avec " + f);
        return m.find();
    }

    @Override
    public boolean controlSize(Path f) {
        String file = f.toString();
        if (file.length() > 0) {
            return true;
        } else {
            System.out.println(f + " => fichier vide");
            return false;
        }
    }

    @Override
    public boolean controlRegex(Path f) {
        int count = 0;
        String error = "ErrorJava == -1";
//        [\w]*(ErrorJava)[\s]*[=]*[\s]*(-1)[\w ]*
        String regex = "(.*)(ErrorJava)[\\s]*[=]{2}[\\s]*(-1)";
        try {
            String content = new String(Files.readAllBytes(f));
            Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(content);
            while (m.find()) {
                int value = Integer.parseInt(m.group(3));
                if (value == -1 && !m.group(1).contains("//")) {
                    count++;
                }
            }
            if (count != 0) {
                System.out.println("Match(s) sur " + count + " ligne(s) pour => " + error + " sur => " + f);
                String logPath = "C:\\Users\\Shadow\\IdeaProjects\\audit\\log";
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
                File logJava = new File(logPath, dateFormat.format(date) + "- logJava.txt");
                FileWriter myWriter = new FileWriter(logJava);
                myWriter.write("Match(s) sur " + count + " ligne(s) pour => " + error + " sur => " + f);
                myWriter.close();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur => " + e);
        }
        return false;
    }
}