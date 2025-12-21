package com.example.movie_rater.config;


import com.example.movie_rater.exception.ApiException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {


    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleException(ApiException apiException){
        return new ResponseEntity(new ErrorMessage(apiException.getMessage()),apiException.getStatus());
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity handleException(BindException e){

        String errorMessage = e.getMessage().substring(0,34);

        return new ResponseEntity(new ErrorMessage(errorMessage), HttpStatus.BAD_REQUEST);
    }

}
