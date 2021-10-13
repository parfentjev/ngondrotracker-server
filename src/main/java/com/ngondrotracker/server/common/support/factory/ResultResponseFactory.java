package com.ngondrotracker.server.common.support.factory;

import com.ngondrotracker.server.common.response.ResultResponse;
import com.ngondrotracker.server.common.support.builder.ResultResponseBuilder;

public class ResultResponseFactory<T> {
    public ResultResponse<T> successful(T result) {
        return new ResultResponseBuilder<T>()
                .withSuccess(true)
                .withResult(result)
                .build();
    }
    public ResultResponse<T> successful(T result, String message) {
        return new ResultResponseBuilder<T>()
                .withSuccess(true)
                .withMessage(message)
                .withResult(result)
                .build();
    }

    public ResultResponse<T> notSuccessful(String message) {
        return new ResultResponseBuilder<T>()
                .withSuccess(false)
                .withMessage(message)
                .build();
    }
}
