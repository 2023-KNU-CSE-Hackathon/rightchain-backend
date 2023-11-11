package com.principes.rightchain.utils.api;

import lombok.Getter;

public class ApiUtil {

    public static <T> ApiSuccessResult<T> success(T response) {
        return new ApiSuccessResult<>(response);
    }

    public static <T> ApiErrorResult<T> error(int code, T message) {
        return new ApiErrorResult<>(code, message);
    }

    @Getter
    public static class ApiSuccessResult<T> {
        private final T response;

        private ApiSuccessResult(T response) {
            this.response = response;
        }

    }

    public static class ApiErrorResult<T> {
        private final int statusCode;
        private final T message;

        private ApiErrorResult(int statusCode, T message) {
            this.statusCode = statusCode;
            this.message = message;
        }

        public int getStatus() {
            return statusCode;
        }

    }

}
