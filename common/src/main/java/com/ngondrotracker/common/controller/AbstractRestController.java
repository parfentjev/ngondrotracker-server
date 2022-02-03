package com.ngondrotracker.common.controller;

public class AbstractRestController {
    protected static final String ANY_ROLE = "hasAnyRole('ADMIN', 'USER', 'NOT_VERIFIED')";
    protected static final String ANY_VERIFIED_ROLE = "hasAnyRole('ADMIN', 'USER')";
    protected static final String ADMIN_ROLE = "hasRole('ADMIN')";
    protected static final String USER_ROLE = "hasRole('USER')";
}
