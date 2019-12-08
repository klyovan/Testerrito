package com.netcracker.testerritto.models;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;


public class Answer {

  private BigInteger id;
  private String textAnswer;
  private int score;
  private BigInteger questionId;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public String getTextAnswer() {
    return textAnswer;
  }

  public void setTextAnswer(String textAnswer) {
    this.textAnswer = textAnswer;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public BigInteger getQuestionId() {
    return questionId;
  }

  public void setQuestionId(BigInteger questionId) {
    this.questionId = questionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Answer answer = (Answer) o;
    return score == answer.score &&
        id.equals(answer.id) &&
        textAnswer.equals(answer.textAnswer) &&
        questionId.equals(answer.questionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, textAnswer, score, questionId);
  }
}
