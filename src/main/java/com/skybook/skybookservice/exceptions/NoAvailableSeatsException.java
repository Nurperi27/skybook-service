package com.skybook.skybookservice.exceptions;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException() {
        super("There are no empty seats on this flight ");
    }
}
