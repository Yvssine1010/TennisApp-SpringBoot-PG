package com.dyma.tennis.rest;

import com.dyma.tennis.service.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xml.sax.ErrorHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.NoSuchElementException;

@RestControllerAdvice /* gérer les exceptions globalement dans une application REST */
public class PlayerControllerErrorHandler  {
    @ExceptionHandler(PlayerNotFoundException.class)/*renvoie une erreur 404 si une ressource (ex. joueur) n’existe pas.*/
    @ResponseStatus(HttpStatus.NOT_FOUND)/*intercepter les exceptions de type NoSuchElementException et de retourner une réponse HTTP 404 Not Found.*/
    public void HandlePlayerNotFoundException(PlayerNotFoundException ex) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, String> HandleValidationException(MethodArgumentNotValidException ex){
        var errors = new HashMap<String,String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName= ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }}