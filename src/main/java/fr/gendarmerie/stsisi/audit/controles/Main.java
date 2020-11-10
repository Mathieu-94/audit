package fr.gendarmerie.stsisi.audit.controles;

import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] argv) {
//        String path = argv[0];
//        System.out.println(path);
        List<IPlugins> mList = Arrays.asList(new GradleControl(), new FichierControl(), new DalvikControl(), new JavaControl());
        try {
            Files.walk(Paths.get("C:\\Users\\Shadow\\Desktop\\Test1")).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> new Thread(() -> {
//            Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> new Thread(() -> {
            for (IPlugins p : mList) {
                if (p.controlName(f)) {
                    if (p.controlSize(f)) {
                        p.controlRegex(f);
                    }
                }
            }
            }).start());
        } catch (Exception e) {
            System.out.println("Erreur parcours dossier => " + e);
        }
    }
}