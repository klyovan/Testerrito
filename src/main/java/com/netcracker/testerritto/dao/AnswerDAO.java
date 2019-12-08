package com.netcracker.testerritto.dao;

import com.netcracker.testerritto.models.Answer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDAO {
  public JdbcTemplate jdbcTemplate;

  public AnswerDAO(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public Answer getAnswer(){}
  public void updateAnswer(Answer answer){}
  public void createAnswer(){}
  public void deleteAnswer(Answer answer){}

}
