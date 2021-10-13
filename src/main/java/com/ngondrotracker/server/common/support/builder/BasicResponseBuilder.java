package com.ngondrotracker.server.common.support.builder;

import com.ngondrotracker.server.common.response.BasicResponse;

public class BasicResponseBuilder {
    private Boolean success;
    private String message;

    public BasicResponse build() {
        BasicResponse response = new BasicResponse();
        response.setSuccess(success);
        response.setMessage(message);

        return response;
    }

    public BasicResponseBuilder withSuccess(boolean isSuccess) {
        this.success = isSuccess;

        return this;
    }

    public BasicResponseBuilder withMessage(String message) {
        this.message = message;

        return this;
    }
}
