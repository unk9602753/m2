package com.epam.esm.controller;

import com.epam.esm.config.Translator;
import com.epam.esm.exception.ServiceException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<Object> handleInvalidDefinitionException(InvalidDefinitionException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", Translator.toLocale("exception.time.in.body"));
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "01");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", Translator.toLocale("exception.column.null"));
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "02");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<Object> handleBadSqlGrammarException(BadSqlGrammarException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", Translator.toLocale("exception.syntax.query"));
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "03");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", ex.getMessage());
        body.put("errorCode", HttpServletResponse.SC_NOT_FOUND + "04");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", Translator.toLocale(e.getMessage()) + e.getId());
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "05");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleBDException(ServiceException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", ex.getMessage());
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "06");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messageError", ex.getMessage());
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "07");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorMessage", Translator.toLocale("exception.parse.json"));
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "08");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorMessage", Translator.toLocale("ex.method.not.support"));
        body.put("errorCode", HttpServletResponse.SC_METHOD_NOT_ALLOWED + "09");
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", HttpServletResponse.SC_METHOD_NOT_ALLOWED + "10");
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", HttpServletResponse.SC_METHOD_NOT_ALLOWED + "11");
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
