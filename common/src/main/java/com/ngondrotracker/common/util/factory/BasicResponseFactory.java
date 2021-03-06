package com.ngondrotracker.common.util.factory;

import com.ngondrotracker.common.response.BasicResponse;
import com.ngondrotracker.common.util.builder.BasicResponseBuilder;

public class BasicResponseFactory {
    public BasicResponse successful() {
        return new BasicResponseBuilder()
                .withSuccess(true)
                .build();
    }
    public BasicResponse successful(String message) {
        return new BasicResponseBuilder()
                .withSuccess(true)
                .withMessage(message)
                .build();
    }

    public BasicResponse notSuccessful(String message) {
        return new BasicResponseBuilder()
                .withSuccess(false)
                .withMessage(message)
                .build();
    }
}
