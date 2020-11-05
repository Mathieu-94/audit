package fr.gendarmerie.stsisi.audit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichierControl implements IPlugins {

    @Override
    public boolean controlName(Path f) {
        String name = "^fichier";
        Pattern r = Pattern.compile(name);
        Matcher m = r.matcher(f.toFile().getName());
        if (m.find()) {
//            System.out.println("\nMatch sur " + this.getClass().getCanonicalName() + " avec " + f);
            return true;
        } else {
            return false;
        }
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
        String error = "minSDK>19";
//        Regex linux => "(\r|\n)\\s*(?!//)\\s*AbortOnError\\s*True"
//        [\w]*(minSDK)[\s]*[>][\s]*(\d*)[\w]*
        String regex = "(.*)(minSDK)[\\s]*[>][\\s]*(\\d*)";
        try {
            String content = new String(Files.readAllBytes(f));
            Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(content);
            while (m.find()) {
                int value = Integer.parseInt(m.group(3));
                if (value > 19 && !m.group(1).contains("//")) {
                    count++;
                }
            }
            if (count != 0) {
                System.out.println("Match(s) sur " + count + " ligne(s)\nPour => " + error + " sur => " + f);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur => " + e);
        }
        return false;
    }
}