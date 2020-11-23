package fr.gendarmerie.stsisi.audit.controles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;
import fr.gendarmerie.stsisi.audit.tools.Tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonControl implements IPlugins {
    @Override
    public boolean controlName(Path f) {
        String name = "json.txt";
        Pattern r = Pattern.compile(name);
        Matcher m = r.matcher(f.toFile().getName());
//        System.out.println("\nMatch sur " + this.getClass().getCanonicalName() + " avec " + f);
        return m.find();
    }

    @Override
    public boolean controlSize(Path f) {
        String file = f.toString();
        return file.length() > 0;
    }

    @Override
    public boolean controlRegex(Path f, String date) {
        try {
            int count = 0;
            String error = "Trackers";
            String content = new String(Files.readAllBytes(f));
            Tools tools = new Tools();

            HttpURLConnection conn = null;
            InputStream is = null;
            JsonObject json = null;

            try {
                URL url = new URL(tools.addressJson()); //Classe qui retourne l'URL du JSon
//            URLConnection request = url.openConnection();
                conn = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(conn.getInputStream());
                conn.connect();
//            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
                JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) conn.getContent()));
                json = root.getAsJsonObject().getAsJsonObject("trackers");
            } catch (Exception ignored) {
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }


            assert json != null;
            Iterator x = json.keySet().iterator();
            JsonArray jsonArray = new JsonArray();

            while (x.hasNext()) {
                String key = (String) x.next();
                jsonArray.add(json.get(key));
            }

//            String strCode2;
            for (JsonElement j : jsonArray) {
                JsonObject jsonObject = j.getAsJsonObject();
                if (((String.valueOf(jsonObject.get("code_signature"))).substring(1, (String.valueOf(jsonObject.get("code_signature"))).length() - 1)).length() > 0) { //J'enleve les " au debut et à la fin
                    String strCode = (String.valueOf(jsonObject.get("code_signature"))).substring(1, (String.valueOf(jsonObject.get("code_signature"))).length() - 1);
//                    System.out.println(strCode);
//                    strCode2 = "(.*)("+strCode+")(.*)";
//                    strCode2 = tools.stringBuilder(strCode);
//                    Pattern r = Pattern.compile(strCode2, Pattern.CASE_INSENSITIVE);
                    Pattern r = Pattern.compile(strCode, Pattern.CASE_INSENSITIVE);
                    Matcher m = r.matcher(content);
                    while (m.find()) {
//                        if (!m.group(1).contains("//")) {
                        count++;
//                        }
                    }
//                }
                }
            }
            if (count != 0) {
                String pathFile = tools.pathFile() + "\\" + date + "-log.txt";
                tools.controlFile(pathFile, "Match(s) sur " + count + " ligne(s) pour => " + error + " sur => " + f);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e);
        }
        return false;
    }
}
