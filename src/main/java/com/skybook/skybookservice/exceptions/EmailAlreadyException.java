package com.skybook.skybookservice.exceptions;

public class EmailAlreadyException extends RuntimeException {
    public EmailAlreadyException() {
        super("Like email already exists");
    }
}
