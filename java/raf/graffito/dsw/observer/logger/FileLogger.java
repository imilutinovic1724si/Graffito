package raf.graffito.dsw.observer.logger;

import raf.graffito.dsw.observer.Message;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLogger extends Logger{

    private static final String LOG_FILE = "src/main/resources/files/log.txt";


    @Override
    protected void log(Message message) {
        try {
            Path logPath = Paths.get(LOG_FILE);
            // ako ne postoji kreiramo direktorijum i fajl
            if (!Files.exists(logPath.getParent())) {
                Files.createDirectories(logPath.getParent());
            }
            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
            }

            // dodavanje poruke u fajl
            try (BufferedWriter writer = Files.newBufferedWriter(logPath, StandardOpenOption.APPEND)) {
                writer.write(message.getFormattedMessage());
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Greska pri upisivanju u fajl: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
