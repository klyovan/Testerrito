package com.netcracker.testerritto.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.models.Reply;

import java.io.IOException;
import java.util.List;

public class ResultMapContentSerializer extends JsonSerializer<Reply> {

    @Override
    public void serialize(Reply reply, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        List<Answer> answers = reply.getReplyList();
        if(answers != null)
            for(Answer answer : answers) {
                jsonGenerator.writeString(answer.getTextAnswer());
            }
        jsonGenerator.writeEndArray();
    }
}
