package com.ngondrotracker.server.common.support.builder;

import com.ngondrotracker.server.common.response.ResultResponse;

public class ResultResponseBuilder<T> {
    private Boolean success;
    private String message;
    private T result;

    public ResultResponse<T> build() {
        ResultResponse<T> response = new ResultResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setResult(result);

        return response;
    }

    public ResultResponseBuilder<T> withSuccess(boolean isSuccess) {
        this.success = isSuccess;

        return this;
    }

    public ResultResponseBuilder<T> withMessage(String message) {
        this.message = message;

        return this;
    }

    public ResultResponseBuilder<T> withResult(T result) {
        this.result = result;

        return this;
    }
}
