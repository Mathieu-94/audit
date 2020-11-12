package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] argv) {
//        String path = argv[0];
//        System.out.println(path);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date oDate = new Date();
        String date = dateFormat.format(oDate);
        List<IPlugins> mList = Arrays.asList(new GradleControl(), new FichierControl(), new DalvikControl(), new JavaControl());
        ArrayList<Boolean> rList = new ArrayList<>();
        try {
            Files.walk(Paths.get("C:\\Users\\Shadow\\Desktop\\Test1")).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> new Thread(() -> {
//            Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> new Thread(() -> {
                for (IPlugins p : mList) {
                    if (p.controlName(f)) {
                        if (p.controlSize(f)) {
                            rList.add(p.controlRegex(f, date));
                        }
                    }
                }
            }).start());
        } catch (Exception e) {
            System.out.println("Erreur parcours dossier => " + e);
        }
        while (Thread.activeCount() != 1) {
            Thread.onSpinWait();
        }
        if (rList.contains(true)) {
            System.exit(-1);
        } else {
            System.exit(0);
        }
    }
}