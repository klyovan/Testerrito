package com.netcracker.testerritto.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Reply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultMapContentDeserializer extends JsonDeserializer {
    @Override
    public Reply deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Reply reply = new Reply();
        List<Answer> answers = new ArrayList<>();
        jsonParser.nextTextValue();
        while(!jsonParser.getText().equals("]")){
            Answer answer = new Answer();
            answer.setTextAnswer(jsonParser.getText());
            answers.add(answer);
            jsonParser.nextTextValue();
        }
        reply.setReplyList(answers);
        return reply;
    }
}
