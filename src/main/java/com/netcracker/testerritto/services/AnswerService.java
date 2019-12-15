package com.netcracker.testerritto.services;


import com.netcracker.testerritto.dao.AnswerDAO;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Answer;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
  @Autowired
  private ServiceExceptionHandler serviceExceptionHandler;
  @Autowired
  private AnswerDAO answerDAO;

  public Answer getAnswerById(BigInteger answerId){return null;}
  public List<Answer> getAllAnswerInQuestion(BigInteger questionId){return null;}
  public Answer getAnswerForReply(BigInteger questionId) {return null;}
  public Answer updateAnswer(Answer answer){return null;}
  public BigInteger createAnswer(Answer newAnswer){return null;}
  public void deleteAnswer(BigInteger answerId){}


}
