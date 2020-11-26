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
    public boolean controlName(Path f) {
        String name = "json.java";
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
        try {
            int count = 0;
            int count2 = 0;
            String error = "Trackers";
            String content = new String(Files.readAllBytes(f));
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
                        if (test != null && test.has("trackers")) {
                            JsonObject trackers = test.getAsJsonObject("trackers");
                            if (trackers != null) {
                                for (String str : trackers.keySet()) {
                                    Tracker tracker = gson.fromJson(trackers.getAsJsonObject(str), Tracker.class);
                                    if (tracker != null) {
//                                        tracker.setDescription("");
                                        mList.add(tracker);
                                    }
                                }
                            } else {
                                count2++;
                            }
                        } else {
                            count2++;
                        }
                    } catch (JsonParseException e) {
                        System.out.println("Erreur de parsing");
                        count2++;
                    }
                } else {
                    System.out.println("Error code status " + conn.getResponseCode());
                }

            } catch (Exception e) {
                System.out.println("Erreur: " + e);
                count2++;
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
            if (count2 != 0) {
                System.out.println("Erreur du Json");
                System.exit(-1);
            }

            String regex;
            for (Tracker track : mList) {
//                System.out.println("Tracker "+track.getName()+" - "+track.getCode_signature());
                List<String> tList = Arrays.asList(track.getCode_signature(), track.getName(), track.getNetwork_signature());
                for (String t : tList) {
                    if (t.length() > 0) {
                        regex = stringBuilder(t);
                        Pattern r = Pattern.compile(regex);
                        Matcher m = r.matcher(content);
                        while (m.find()) {
                            if (!m.group(1).contains("//")) {
                                tools.controlFile(error, t, f);
                                count++;
                            }
                        }
                    }
                }
            }
            if (count != 0) {
//                tools.controlFile(count, error, f);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur: " + e);
        }
        return false;
    }

    private String stringBuilder(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("(.*)(").append(s).append(")(.*)");
        return sb.toString();
    }
}
