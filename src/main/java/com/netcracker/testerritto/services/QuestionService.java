package com.netcracker.testerritto.services;



import com.netcracker.testerritto.dao.QuestionDAO;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Question;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
  @Autowired
  private ServiceExceptionHandler serviceExceptionHandler;
  @Autowired
  private QuestionDAO questionDAO;

  public Question getQuestionById(BigInteger id) {return null;}
  public List<Question> getAllQuestionInTest(BigInteger test_id){return null;}
  public Question updateQuestion(Question question){return null;}
  public BigInteger createQuestion(Question newQuestion){return null;}
  public void deleteQuestionById(BigInteger id){}



  }
