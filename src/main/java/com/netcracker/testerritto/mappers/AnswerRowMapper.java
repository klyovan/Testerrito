package com.netcracker.testerritto.mappers;

import com.netcracker.testerritto.models.Answer;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AnswerRowMapper implements RowMapper<Answer> {

  @Override
  public Answer mapRow(ResultSet resultSet, int i) throws SQLException {
    Answer answer = new Answer();
    answer.setId(new BigInteger(resultSet.getString("id")));
    answer.setQuestionId(new BigInteger(resultSet.getString("questionId")));
    answer.setScore(resultSet.getInt("score"));
    answer.setTextAnswer(resultSet.getString("text"));
    return answer;
  }
}
