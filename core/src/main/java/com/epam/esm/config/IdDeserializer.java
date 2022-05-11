package com.epam.esm.config;

import com.epam.esm.exception.DeserializeException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;

public class IdDeserializer extends StdDeserializer {
    public IdDeserializer() {
        this(null);
    }

    public IdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    @SneakyThrows(DeserializeException.class)
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String id = p.getText();
        if (id != null) {
            throw new DeserializeException("exception.parse.id");
        }
        return 0L;
    }
}
