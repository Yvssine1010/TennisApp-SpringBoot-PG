package com.dyma.tennis.service;

public class PlayerNotFoundException extends RuntimeException {  /*exception lever lors runtime*/
    public PlayerNotFoundException(String lastName) {
        super("Player not found: " + lastName);
    }
}
