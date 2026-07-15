package raf.graffito.dsw.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;
import java.io.IOException;

// Kako učitati Dimension
public class DimensionDeserializer extends JsonDeserializer<Dimension> {
    @Override
    public Dimension deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        int width = node.get("width").asInt();
        int height = node.get("height").asInt();
        return new Dimension(width, height);
    }
}