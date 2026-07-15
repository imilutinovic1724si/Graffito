package raf.graffito.dsw.serializer;

import raf.graffito.dsw.model.implementation.Project;

import java.io.File;

// primena: Strategy Pattern
public interface Serializer {
    /**
     * Serijalizuje projekat u fajl
     * @param project Projekat koji se serijalizuje
     * @param file Fajl gde se čuva
     * @throws Exception ako serijalizacija ne uspe
     */
    void saveProject(Project project, File file) throws Exception;
    /**
     * Deserijalizuje projekat iz fajla
     * @param file Fajl odakle se učitava
     * @return Učitan projekat
     * @throws Exception ako deserijalizacija ne uspe
     */
    Project loadProject(File file) throws Exception;
}