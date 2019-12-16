package com.netcracker.testerritto.handlers;

import com.netcracker.testerritto.exceptions.ApiException;
import com.netcracker.testerritto.exceptions.ApiRequestException;
import com.netcracker.testerritto.exceptions.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    private static Logger logger = Logger.getLogger(ApiExceptionHandler.class.getName());

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleServiceException(ApiRequestException e) {
        logger.error(e.getMessage(), e);
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }


    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<Object> handleInternalError(ServiceException e) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
}
