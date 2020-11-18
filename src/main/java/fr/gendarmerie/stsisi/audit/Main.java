package fr.gendarmerie.stsisi.audit;


import fr.gendarmerie.stsisi.audit.controles.*;
import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] argv) throws InterruptedException {
        if (argv.length != 1) {
            System.out.println("Erreur => 1 seul argument requis (chemin du dossier)");
            System.exit(0);
        }
        String path = argv[0];
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date oDate = new Date();
        String date = dateFormat.format(oDate);
        List<IPlugins> mList = Arrays.asList(new GradleControl(), new FichierControl(), new DalvikControl(), new JavaControl(), new JsonControl());
//        List<IPlugins> mList = Arrays.asList(new JsonControl());
        AtomicBoolean b = new AtomicBoolean(false);
        try {
            Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> new Thread(() -> {
                for (IPlugins p : mList) {
                    if (p.controlName(f)) {
                        if (p.controlSize(f)) {
                            if (p.controlRegex(f, date)) {
                                b.set(true);
                            }
                        }
                    }
                }
            }).start());
        } catch (Exception e) {
            System.out.println("Erreur parcours dossier => " + e);
        }
        while (Thread.activeCount() != 1) {
            Thread.sleep(0,5);
        }
        if (b.get()) {
            System.exit(-1);
        } else {
            System.out.println("Aucune erreur detectée");
            System.exit(0);
        }


//
//        Tools tools = new Tools();
//        URL url = new URL(tools.addressJson()); //Classe qui retourne l'URL du JSon
//        URLConnection request = url.openConnection();
//        request.connect();
//        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
//        JsonObject json = root.getAsJsonObject().getAsJsonObject("trackers");
//
//        Iterator x = json.keySet().iterator();
//        JsonArray jsonArray = new JsonArray();
//
//        while (x.hasNext()) {
//            String key = (String) x.next();
//            jsonArray.add(json.get(key));
//        }
//
//        for (JsonElement j:jsonArray) {
//                JsonObject codeSignature = j.getAsJsonObject();
//            if (((String.valueOf(codeSignature.get("code_signature"))).substring(1, (String.valueOf(codeSignature.get("code_signature"))).length()-1)).length() > 0) {
//                String strCode = (String.valueOf(codeSignature.get("code_signature"))).substring(1, (String.valueOf(codeSignature.get("code_signature"))).length() - 1);
//                System.out.println(strCode);
////                if (strCode.contains("|")) {
////                    System.out.println("ok");
////                }
//            }
//        }


    }
}