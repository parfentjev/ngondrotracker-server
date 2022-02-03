package com.ngondrotracker.common.exception;

public class ItemAlreadyExistsException extends Exception {
    public ItemAlreadyExistsException() {
        super("ALREADY_EXISTS");
    }
}
