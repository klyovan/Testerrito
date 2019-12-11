package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Answer;
import com.netcracker.testerritto.properties.ListsAttr;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AnswerRowMapper implements RowMapper<Answer> {

  @Override
  public Answer mapRow(ResultSet resultSet, int i) throws SQLException {
    Answer answer = new Answer();
    answer.setId(new BigInteger(resultSet.getString("id")));
    answer.setQuestionId(new BigInteger(resultSet.getString("question_id")));
    answer.setScore(resultSet.getInt("score"));
    answer.setTextAnswer(ListsAttr.getValueById(BigInteger.valueOf(resultSet.getInt("text"))));
    return answer;
  }
}
