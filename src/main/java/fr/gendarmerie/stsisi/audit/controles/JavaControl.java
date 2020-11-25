package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaControl implements IPlugins {

    @Override
    public boolean controlName(Path f) {
        String name = "[\\w*].java";
        Pattern r = Pattern.compile(name);
        Matcher m = r.matcher(f.toFile().getName());
        return m.find();
    }

    @Override
    public boolean controlSize(Path f) {
        String file = f.toString();
        return file.length() > 0;
    }

    @Override
    public boolean controlRegex(Path f) {
        int count = 0;
        String error = "ErrorJava == -1";
        String regex = "(.*)(ErrorJava)[\\s]*[=]{2}[\\s]*(-1)";
        Tools tools = Tools.getInstance();
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
                tools.controlFile(count, error, f);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur => " + e);
        }
        return false;
    }
}