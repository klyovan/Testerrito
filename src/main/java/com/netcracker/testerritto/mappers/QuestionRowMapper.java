package com.netcracker.testerritto.mappers;

import org.springframework.jdbc.core.RowMapper;
import com.netcracker.testerritto.models.Question;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class QuestionRowMapper implements RowMapper<Question> {
    @Override
    public Question mapRow(ResultSet resultSet, int i) throws SQLException {
        Question question = new Question();
        question.setId(new BigInteger(resultSet.getString("id")));
        question.setTextQuestion(resultSet.getString("text"));
        question.setTypeQuestion(resultSet.getString("type"));
        question.setTestId(new BigInteger(resultSet.getString("testId")));
        return question;
    }
}
