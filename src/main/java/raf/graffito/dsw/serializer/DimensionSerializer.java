package raf.graffito.dsw.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.awt.*;
import java.io.IOException;

// Kako sačuvati Dimension
public class DimensionSerializer extends JsonSerializer<Dimension> {
    @Override
    public void serialize(Dimension dimension, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("width", dimension.width);
        gen.writeNumberField("height", dimension.height);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(Dimension value, JsonGenerator gen,SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
            serialize(value, gen, serializers);
        }
}