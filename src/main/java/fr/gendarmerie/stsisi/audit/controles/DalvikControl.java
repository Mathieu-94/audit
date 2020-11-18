package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DalvikControl implements IPlugins {
    @Override
    public boolean controlName(Path f) {
        String name = ".java|kt";
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
        int count1 = 0;
        int count2 = 0;
        String error1 = "import dalvik.system.DexClassLoader";
        String importDalvik = "(.*)import[\\s]*dalvik.system.DexClassLoader";
        String error2 = "new dalvik.system.DexClassLoader.DexClassLoader(...)";
        String utilisationDalvik = "(.*)new[\\s]*dalvik.system.DexClassLoader.DexClassLoader\\(.*\\)";
        try {
            String content = new String(Files.readAllBytes(f));
            Pattern r = Pattern.compile(importDalvik, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(content);
            Pattern r2 = Pattern.compile(utilisationDalvik, Pattern.CASE_INSENSITIVE);
            Matcher m2 = r2.matcher(content);
            while (m.find()) {
                if (!m.group(1).contains("//")) {
                    count1++;
                }
            }
            while (m2.find()) {
                if (!m2.group(1).contains("//")) {
                    count2++;
                }
            }
            if (count1 != 0 || count2 !=0) {
                Tools tools = new Tools();
                String pathFile = tools.pathFile()+"\\"+date+"-log.txt" ;
                if (count1 != 0) {
                    tools.controlFile(pathFile, "Match(s) sur " + count1 + " ligne(s) pour => " + error1 + " sur => " + f);
                }
                if (count2 != 0) {
                    tools.controlFile(pathFile, "Match(s) sur " + count2 + " ligne(s) pour => " + error2 + " sur => " + f);
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur => " + e);
        }
        return false;
    }
}