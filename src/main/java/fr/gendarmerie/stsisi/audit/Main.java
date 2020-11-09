package fr.gendarmerie.stsisi.audit;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] argv) {
//        String path = argv[0];
//        System.out.println(path);
        List<IPlugins> mList = Arrays.asList(new GradleControl(), new FichierControl(), new JavaControl(), new DalvikControl());
        ArrayList<Boolean> rList = new ArrayList<>();
        try {
//            long start = System.currentTimeMillis();
            Files.walk(Paths.get("C:\\Users\\Shadow\\Desktop\\Test1")).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> {
//                new Thread(() -> {
                for (IPlugins p : mList) {
                    if (p.controlName(f)) {
                        if (p.controlSize(f)) {
                            rList.add(p.controlRegex(f));
                        }
                    }
                }
//                }).start();
            });
//            long stop = System.currentTimeMillis() - start;
//            System.out.println("Time : " + stop);
        } catch (Exception e) {
            System.out.println("Erreur parcours dossier => " + e);
        }
        if (rList.contains(true)) {
            System.exit(-1);
        } else {
            System.out.println("Aucune erreur !");
            System.exit(0);
        }
    }
}