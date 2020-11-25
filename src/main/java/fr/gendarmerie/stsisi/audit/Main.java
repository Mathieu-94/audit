package fr.gendarmerie.stsisi.audit;


import fr.gendarmerie.stsisi.audit.controles.*;
import fr.gendarmerie.stsisi.audit.interfaces.IPlugins;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.out.println("Erreur => 1 seul argument requis (chemin du dossier)");
            System.exit(-1);
        }
        String path = argv[0];
        List<IPlugins> mList = Arrays.asList(new GradleControl(), new FichierControl(), new DalvikControl(), new JavaControl(), new JsonControl());
        AtomicBoolean b = new AtomicBoolean(false);
        try {
            Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(Files::isReadable).filter(Files::isWritable).forEach((f) -> new Thread(() -> {
                for (IPlugins p : mList) {
                    if (p.controlName(f)) {
                        if (p.controlSize(f)) {
                            if (p.controlRegex(f)) {
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
            try {
                Thread.sleep(0, 5);
            } catch (Exception e) {
                System.out.println("Erreur: " + e);
            }
        }
        if (b.get()) {
            System.exit(-1);
        } else {
            System.out.println("Aucune erreur detectée");
            System.exit(0);
        }
    }
}