package com.ngondrotracker.common.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final String resource;

    public ResourceAlreadyExistsException(String resource) {
        super(String.format("%s already exists", resource));
        this.resource = resource;
    }
}
