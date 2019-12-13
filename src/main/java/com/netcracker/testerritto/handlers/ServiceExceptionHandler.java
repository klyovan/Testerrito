package com.netcracker.testerritto.handlers;

import com.netcracker.testerritto.exceptions.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ServiceExceptionHandler {
    private static Logger logger = Logger.getLogger(ServiceExceptionHandler.class.getName());

    public void logAndThrowIllegalException(String message) throws IllegalArgumentException {
        IllegalArgumentException ex = new IllegalArgumentException(message);
        logger.error(ex.getMessage(), ex);
        throw ex;
    }

    public void logAndThrowServiceException(String message, Throwable cause) throws ServiceException {
        ServiceException ex = new ServiceException(message, cause);
        logger.error(ex.getMessage(), ex);
        throw ex;
    }
}
