package raf.graffito.dsw.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import raf.graffito.dsw.model.implementation.Project;

import java.awt.*;
import java.io.File;

public class SerializerJSON implements Serializer {

    private ObjectMapper objectMapper; // Jackson biblioteka za JSON

    public SerializerJSON() {
        this.objectMapper = new ObjectMapper();

        // lepo formatiran JSON sa uvlačenjem
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // ignoriši nepoznate properties
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ne serijalizuj null vrednosti
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

        // dozvoli polimorfizam
        PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder().allowIfSubType("raf.graffito.dsw.model").allowIfSubType("java.util").build();
        objectMapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);

        // Registruj custom serializere za Point i Dimension
        SimpleModule module = new SimpleModule();
        module.addSerializer(Point.class, new raf.graffito.dsw.serializer.PointSerializer());
        module.addDeserializer(Point.class, new raf.graffito.dsw.serializer.PointDeserializer());
        module.addSerializer(Dimension.class, new raf.graffito.dsw.serializer.DimensionSerializer());
        module.addDeserializer(Dimension.class, new raf.graffito.dsw.serializer.DimensionDeserializer());
        objectMapper.registerModule(module);

        System.out.println("SerializerJSON inicijalizovan");
    }

    @Override
    public void saveProject(Project project, File file) throws Exception {
        if (project == null) {
            throw new IllegalArgumentException("Projekat ne može biti null");
        }
        if (file == null) {
            throw new IllegalArgumentException("Fajl ne može biti null");
        }

        System.out.println("Serijalizacija projekta: " + project.getName() + " u fajl: " + file.getAbsolutePath());

        // Pretvori projekat u JSON i sačuvaj ga
        objectMapper.writeValue(file, project);

        System.out.println("Projekat uspešno sačuvan!");
    }

    @Override
    public Project loadProject(File file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("Fajl ne može biti null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("Fajl ne postoji: " + file.getAbsolutePath());
        }

        System.out.println("Deserijalizacija projekta iz fajla: " + file.getAbsolutePath());

        // učitaj JSON i pretvori ga u projekat
        Project project = objectMapper.readValue(file, Project.class);

        System.out.println("Projekat uspešno učitan: " + project.getName());

        return project;
    }
}