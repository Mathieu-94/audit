package fr.gendarmerie.stsisi.audit;

import java.nio.file.Path;

public interface IPlugins {
    boolean controlSize(Path f);

    boolean controlRegex(Path f);

    boolean controlName(Path f);
}