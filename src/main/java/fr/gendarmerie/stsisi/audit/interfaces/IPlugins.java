package fr.gendarmerie.stsisi.audit.interfaces;

import java.nio.file.Path;

public interface IPlugins {
    boolean controlName(Path filePath);

    boolean controlSize(Path filePath);

    boolean controlRegex(Path filePath);
}