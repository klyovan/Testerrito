package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.RemarkDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class RemarkService {

    @Autowired
    private RemarkDAO remarkDAO;

    private static Logger log=Logger.getLogger(RemarkService.class.getName());

    public BigInteger createRemark(BigInteger userId, BigInteger questionId, String remarkText){
        if(checkNotNull(userId) && checkNotNull(questionId) && !"".equals(remarkText))
            return remarkDAO.createRemark(userId, questionId, remarkText);
        else {
            IllegalArgumentException exception = new IllegalArgumentException("Parametrs(userId, questionId, remarkText) can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage());
            throw exception;
        }
    }

    public void deleteRemark(BigInteger remarkId){
        if(checkNotNull(remarkId))
            remarkDAO.deleteRemark(remarkId);
        else {
            IllegalArgumentException exception = new IllegalArgumentException("Parametr RemarkID can not be NULL.");
            log.log(Level.SEVERE, exception.getMessage());
            throw exception;
        }
    }

    private boolean checkNotNull(BigInteger id){
        if(id != null)
            return true;
        return false;

    }
}
