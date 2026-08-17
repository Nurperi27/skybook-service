package com.skybook.skybookservice.exceptions;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException(Long flightId) {
        super("There are no empty seats on this flight ");
    }
}
