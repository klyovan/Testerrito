package com.netcracker.testerritto.services;

import com.netcracker.testerritto.dao.RemarkDAO;
import com.netcracker.testerritto.exceptions.ServiceException;
import com.netcracker.testerritto.handlers.ServiceExceptionHandler;
import com.netcracker.testerritto.models.Remark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.math.BigInteger;

@Service
public class RemarkService {

    @Autowired
    private RemarkDAO remarkDAO;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    public Remark getRemarkById(BigInteger remarkId) throws ServiceException {
        checkIdNotNull(remarkId);
        try {
            return remarkDAO.getRemarkById(remarkId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed GetRemarkById().", exception);
            return null;
        }
    }

    public BigInteger createRemark(Remark remark) throws ServiceException {
        checkIdNotNull(remark.getUserSenderId());
        checkIdNotNull(remark.getQuestionId());
        checkIdNotNull(remark.getUserRecipientId());
        checkStringNotNull(remark.getText());
        try {
            return remarkDAO.createRemark(remark);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed CreateRemark().", exception);
            return null;
        }
    }

    public void updateRemarkViewStatus(BigInteger remarkId) {
        checkIdNotNull(remarkId);
        try {
            remarkDAO.updateRemarkViewStatus(remarkId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed UpdateViewStatus().", exception);
        }
    }

    public void deleteRemark(BigInteger remarkId) throws ServiceException {
        checkIdNotNull(remarkId);
        try {
            remarkDAO.deleteRemark(remarkId);
        } catch (DataAccessException exception) {
            serviceExceptionHandler.logAndThrowServiceException("Failed DeleteRemark().", exception);
        }
    }

    private void checkIdNotNull(BigInteger id) {
        if (id == null)
           serviceExceptionHandler.logAndThrowIllegalException("Parameter ID can not be NULL");
    }

    private void checkStringNotNull(String string) {
        if ("".equals(string) || string == null)
            serviceExceptionHandler.logAndThrowIllegalException("String parameter can not be NULL OR EMPTY.");
    }


}
