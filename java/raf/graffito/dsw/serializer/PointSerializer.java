package raf.graffito.dsw.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.awt.*;
import java.io.IOException;

// Kako sačuvati Point pošto ga JSON ne prepoznaje
public class PointSerializer extends JsonSerializer<Point> {
    @Override
    public void serialize(Point point, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", point.x);
        gen.writeNumberField("y", point.y);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(Point value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
             serialize(value, gen, serializers);
            }
}