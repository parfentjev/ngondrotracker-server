package com.ngondrotracker.common.exception;

public class ItemDoesNotExist extends RuntimeException {
    public ItemDoesNotExist() {
        super("DOES_NOT_EXIST");
    }
}
