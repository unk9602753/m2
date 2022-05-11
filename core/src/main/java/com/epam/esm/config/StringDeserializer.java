package com.epam.esm.config;

import com.epam.esm.exception.DeserializeException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;

public class StringDeserializer extends StdDeserializer<String> {
    public StringDeserializer() {
        this(null);
    }

    public StringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    @SneakyThrows(DeserializeException.class)
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (p.getCurrentToken() == JsonToken.VALUE_NUMBER_INT) {
            throw new DeserializeException("exception.parse.string");
        }
        return p.getValueAsString();
    }
}
