package com.netcracker.testerritto.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.netcracker.testerritto.models.Question;

import java.io.IOException;

public class ResultMapKeySerializer extends JsonSerializer<Question> {

    @Override
    public void serialize(Question question, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeFieldName(question.getTextQuestion());
    }
}
