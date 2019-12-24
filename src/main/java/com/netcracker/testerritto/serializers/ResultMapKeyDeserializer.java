package com.netcracker.testerritto.serializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.netcracker.testerritto.models.Question;

import java.io.IOException;

public class ResultMapKeyDeserializer extends KeyDeserializer {

    @Override
    public Question deserializeKey(String key, DeserializationContext deserializationContext) throws IOException {
        Question question = new Question();
        question.setTextQuestion(key);
        return question;
    }
}
