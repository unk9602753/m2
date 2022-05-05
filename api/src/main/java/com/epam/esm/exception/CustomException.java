package com.epam.esm.exception;

import com.epam.esm.config.Translator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNodataFoundException(NoSuchElementException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("m", ex.getMessage());
        body.put("errorCode", HttpServletResponse.SC_NOT_FOUND + "01");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MysqlDataTruncation.class)
    public ResponseEntity<Object> sqlException(MysqlDataTruncation ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("2message", ex.getMessage());
        body.put("2code", HttpServletResponse.SC_BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLNonTransientException.class)
    public ResponseEntity<Object> nonTransientException(SQLNonTransientException ex, HttpServletResponse res) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("3message", ex.getMessage());
        body.put("3code", res.getStatus());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleConflict(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("4message", ex.getMessage());
        body.put("4code", HttpServletResponse.SC_BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleConflict(SQLIntegrityConstraintViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("41message", ex.getMessage());
        body.put("41code", HttpServletResponse.SC_BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<Object> handleConflict(InvalidDefinitionException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("5message", ex.getMessage());
        body.put("5code", HttpServletResponse.SC_BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> handleConflict(JsonParseException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("6message", ex.getMessage());
        body.put("6code", HttpServletResponse.SC_BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorMessage", Translator.toLocale("ex.parse.json"));
        body.put("errorCode", HttpServletResponse.SC_BAD_REQUEST + "01");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedExceptionException(MethodNotAllowedException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("7message", ex.getMessage());
        body.put("7code", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("71message", Translator.toLocale("ex.method.not.support"));
        body.put("71code", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("1711message", ex.getMessage());
        body.put("1711code", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //////////////////////////////////////////////////////
    public CustomException() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("100000000000message", ex.getMessage());
        body.put("1000000000000code", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("1111111111message", ex.getMessage());
        body.put("1111111111111", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("22222222222222", ex.getMessage());
        body.put("22222222222222", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("33333333333", ex.getMessage());
        body.put("3333333333", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("44444444", ex.getMessage());
        body.put("444444444444", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("5555555555", ex.getMessage());
        body.put("5555555555", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("666666666", Translator.toLocale("ex.url.mismatch"));
        body.put("6666666666", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("777777", ex.getMessage());
        body.put("7777777", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("8888888888", ex.getMessage());
        body.put("888888", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("999999999", ex.getMessage());
        body.put("999999999", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("aaaaaaaaaaa", "f");
        body.put("aaaaaaaaaa", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ddddddddddd", ex.getMessage());
        body.put("dddddddd", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body1 = new LinkedHashMap<>();
        body1.put("cccccccccc", ex.getMessage());
        body1.put("ccccccccc", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body1, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
