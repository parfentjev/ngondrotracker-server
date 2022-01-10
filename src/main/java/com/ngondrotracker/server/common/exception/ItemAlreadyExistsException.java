package com.ngondrotracker.server.common.exception;

public class ItemAlreadyExistsException extends Exception {
    public ItemAlreadyExistsException() {
        super("ALREADY_EXISTS");
    }
}
