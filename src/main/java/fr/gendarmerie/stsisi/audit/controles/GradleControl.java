package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradleControl implements IPlugins {

    @Override
    public boolean controlName(Path f) {
        String name = "^build.gradle";
        Pattern r = Pattern.compile(name);
        Matcher m = r.matcher(f.toFile().getName());
        //            System.out.println("\nMatch sur " + this.getClass().getCanonicalName() + " avec " + f);
        return m.find();
    }

    @Override
    public boolean controlSize(Path f) {
        String file = f.toString();
        return file.length() > 0;
    }

    @Override
    public boolean controlRegex(Path f, String date) {
        int count = 0;
        String error = "AbortOnError True";
//        Regex linux => "(\r|\n)\\s*(?!//)\\s*AbortOnError\\s*True"
        String regex = "(.*)(AbortOnError)\\s*(True)";
        try {
            String content = new String(Files.readAllBytes(f));
            Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(content);
            while (m.find()) {
                if (!m.group(1).contains("//")) {
                    count++;
                }
            }
            if (count != 0) {
                Tools tools = new Tools();
                String pathFile = tools.pathFile() + "\\" + date + "-log.txt";
                tools.controlFile(pathFile, "Match(s) sur " + count + " ligne(s) pour => " + error + " sur => " + f);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur => " + e);
        }
        return false;
    }
}