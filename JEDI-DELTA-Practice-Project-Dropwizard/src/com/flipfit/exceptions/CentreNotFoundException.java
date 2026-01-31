package com.flipfit.exceptions;

public class CentreNotFoundException extends Exception {
    // ANSI escape codes for colored console output
    private static final String RED_COLOR = "\u001B[31m";
    private static final String RESET_COLOR = "\u001B[0m";

    // Constructor name must match the Class name exactly
    public CentreNotFoundException(String gymId) {
        super(RED_COLOR + "Gym Centre " + gymId + " not found!" + RESET_COLOR);
    }
}