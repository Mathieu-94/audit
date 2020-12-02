package fr.gendarmerie.stsisi.audit.controles;

import com.google.gson.*;
import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.pojo.Tracker;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonControl implements IPlugins {
    @Override
    public boolean controlName(Path filePath) {
        String fileName = "json.java";
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
        String typeError = "Trackers";
        try {
            String stringFileContent = new String(Files.readAllBytes(filePath));
            Tools tools = Tools.getInstance();

            HttpURLConnection conn = null;
            InputStream is = null;
            ArrayList<Tracker> mList = new ArrayList<>();

            try {
                URL url = new URL("https://reports.exodus-privacy.eu.org/api/trackers");
                conn = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(conn.getInputStream());
                conn.connect();

                if (conn.getResponseCode() == 200) {
                    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                    Gson gson = new Gson();
                    try {
                        JsonObject test = gson.fromJson(reader, JsonObject.class);
                        if (test != null && test.has("trackers") && test.getAsJsonObject("trackers") != null) {
                            JsonObject trackers = test.getAsJsonObject("trackers");
                            for (String str : trackers.keySet()) {
                                Tracker tracker = gson.fromJson(trackers.getAsJsonObject(str), Tracker.class);
                                if (tracker != null) {
                                    mList.add(tracker);
                                }
                            }
                        } else {
                            isStringFound = true;
                        }
                    } catch (JsonParseException e) {
                        System.out.println("Erreur de parsing: " + e);
                        isStringFound = true;
                    }
                } else {
                    System.out.println("Error code status " + conn.getResponseCode());
                }

            } catch (Exception e) {
                System.out.println("Erreur: " + e);
                isStringFound = true;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        System.out.println("Erreur: " + e);
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }

            if (isStringFound) {
                System.out.println("Erreur du Json");
                System.exit(-1);
            }

            isStringFound = false;
            String regex;
            for (Tracker track : mList) {
                List<String> tList = Arrays.asList(track.getCodeSignature(), track.getName(), track.getNetworkSignature());
                for (String nameError : tList) {
                    if (nameError.length() > 0) {
                        regex = myStringBuilder(nameError);
                        Pattern patternRegex = Pattern.compile(regex);
                        Matcher matcherRegex = patternRegex.matcher(stringFileContent);
                        while (matcherRegex.find()) {
                            if (!matcherRegex.group(1).contains("//")) {
                                tools.controlFile(typeError, nameError, filePath);
                                isStringFound = true;
                            }
                        }
                    }
                }
            }
            if (isStringFound) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur: " + e);
        }
        return false;
    }

    private String myStringBuilder(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("(.*)(").append(s).append(")(.*)");
        return sb.toString();
    }
}
