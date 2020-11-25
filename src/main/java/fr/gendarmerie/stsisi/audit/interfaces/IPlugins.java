package fr.gendarmerie.stsisi.audit.interfaces;

import java.nio.file.Path;

public interface IPlugins {
    boolean controlName(Path f);

    boolean controlSize(Path f);

    boolean controlRegex(Path f);
}