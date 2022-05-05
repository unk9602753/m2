package com.epam.esm.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class IdDeserializer extends StdDeserializer {
    public IdDeserializer() {
        this(null);
    }

    public IdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String id = p.getText();
        if (id != null) {
            throw new RuntimeException(Translator.toLocale("ex.id.not.null"), null);
        }
        return 0L;
    }
}
