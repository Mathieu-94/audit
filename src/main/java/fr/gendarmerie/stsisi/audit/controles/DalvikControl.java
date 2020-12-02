package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DalvikControl implements IPlugins {
    @Override
    synchronized public boolean controlName(Path filePath) {
        String fileName = ".java|kt";
        Pattern patternControlName = Pattern.compile(fileName);
        Matcher matcherControlName = patternControlName.matcher(filePath.toFile().getName());
        return matcherControlName.find();
    }

    @Override
    public boolean controlSize(Path filePath) {
        String fileString = filePath.toString();
        return fileString.length() > 0;
    }

    @Override
    public boolean controlRegex(Path filePath) {
        boolean isStringFound = false;
        String typeError = "Erreur";
        String nameErrorImportDalvik = "import dalvik.system.DexClassLoader";
        String regexImportDalvik = "(.*)import[\\s]*dalvik.system.DexClassLoader";
        String nameErrorUtilisationDalvik = "new dalvik.system.DexClassLoader.DexClassLoader(...)";
        String regexUtilisationDalvik = "(.*)new[\\s]*dalvik.system.DexClassLoader.DexClassLoader\\(.*\\)";
        Tools tools = Tools.getInstance();
        try {
            String stringFileContent = new String(Files.readAllBytes(filePath));
            Pattern patternRegexImportDalvik = Pattern.compile(regexImportDalvik, Pattern.CASE_INSENSITIVE);
            Matcher matcherRegexImportDalvik = patternRegexImportDalvik.matcher(stringFileContent);
            Pattern patternRegexUtilisationDalvik = Pattern.compile(regexUtilisationDalvik, Pattern.CASE_INSENSITIVE);
            Matcher matcherRegexUtilisationDalvik = patternRegexUtilisationDalvik.matcher(stringFileContent);
            while (matcherRegexImportDalvik.find()) {
                if (!matcherRegexImportDalvik.group(1).contains("//")) {
                    tools.controlFile(typeError, nameErrorImportDalvik, filePath);
                    isStringFound = true;
                }
            }
            while (matcherRegexUtilisationDalvik.find()) {
                if (!matcherRegexUtilisationDalvik.group(1).contains("//")) {
                    tools.controlFile(typeError, nameErrorUtilisationDalvik, filePath);
                    isStringFound = true;
                }
            }
            if (isStringFound) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e);
        }
        return false;
    }
}