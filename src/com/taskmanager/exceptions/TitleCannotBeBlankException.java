package com.taskmanager.exceptions;

public class TitleCannotBeBlankException extends RuntimeException {
    public TitleCannotBeBlankException(String message) {
        super(message);
    }
}
