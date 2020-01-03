package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.properties.ListsAttr;
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
        question.setTypeQuestion(ListsAttr.getValueById(BigInteger. valueOf(resultSet.getInt("question_type"))));
        question.setTestId(new BigInteger(resultSet.getString("test_id")));
        question.setCategoryId(new BigInteger(resultSet.getString("category_id")));
        return question;
    }
}
