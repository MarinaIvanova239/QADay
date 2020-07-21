package com.dins;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public abstract class ToJSON {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return Objects.toString(this);
        }
    }
}
