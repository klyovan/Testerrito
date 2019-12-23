package com.netcracker.testerritto.models;


import java.math.BigInteger;
import java.util.Objects;


public class Answer extends ObjectEav{

    private String textAnswer;
    private int score;
    private BigInteger questionId;
    private BigInteger nextQuestionId;
    private BigInteger replyId;

    public BigInteger getReplyId() {
        return replyId;
  }

    public void setReplyId(BigInteger replyId) {
        this.replyId = replyId;
    }

    public BigInteger getNextQuestionId() {
        return nextQuestionId;
    }

    public void setNextQuestionId(BigInteger nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
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
