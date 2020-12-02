package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichierControl implements IPlugins {

    @Override
    synchronized public boolean controlName(Path filePath) {
        String fileName = "^fichier";
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
        String nameError = "minSDK>19";
        String regexError = "(.*)(minSDK)[\\s]*[>][\\s]*(\\d*)";
        Tools tools = Tools.getInstance();
        try {
            String stringFileContent = new String(Files.readAllBytes(filePath));
            Pattern patternRegex = Pattern.compile(regexError, Pattern.CASE_INSENSITIVE);
            Matcher matcherRegex = patternRegex.matcher(stringFileContent);
            while (matcherRegex.find()) {
                int value = Integer.parseInt(matcherRegex.group(3));
                if (value > 19 && !matcherRegex.group(1).contains("//")) {
                    tools.controlFile(typeError, nameError, filePath);
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