package com.example.movie_rater.config;


import com.example.movie_rater.exception.ApiException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice {


    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleException(ApiException apiException){
        return new ResponseEntity(new ErrorMessage(apiException.getMessage()),apiException.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        Map<String, String> body = new HashMap<>();
        body.put("message", message);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity handleException(BindException e){

        String errorMessage = e.getMessage().substring(0,34);

        return new ResponseEntity(new ErrorMessage(errorMessage), HttpStatus.BAD_REQUEST);
    }

}
