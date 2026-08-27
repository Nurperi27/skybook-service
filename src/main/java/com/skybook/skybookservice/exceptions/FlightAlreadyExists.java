package com.skybook.skybookservice.exceptions;

public class FlightAlreadyExists extends RuntimeException {
    public FlightAlreadyExists() {
        super("Рейс с таким номером уже есть");
    }
}
