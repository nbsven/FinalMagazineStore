package com.epam.magazinestore.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateToStringSerializer extends StdSerializer<LocalDateTime> {

    public static final String FORMAT = "dd-MM-yyyy HH-mm-ss";

    public LocalDateToStringSerializer() {
        this(null);
    }

    public LocalDateToStringSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ofPattern(FORMAT)));
    }
}
