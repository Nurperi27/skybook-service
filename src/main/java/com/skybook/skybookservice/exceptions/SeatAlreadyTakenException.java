package com.skybook.skybookservice.exceptions;

public class SeatAlreadyTakenException extends RuntimeException {
    public SeatAlreadyTakenException() {
        super("This Seat number is already taken");
    }
}
