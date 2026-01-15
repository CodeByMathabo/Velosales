package com.codebymathabo.common;

import java.time.LocalDateTime;

// Generic wrapper ensuring uniform JSON structure for all API endpoints.
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Method for successful responses to keep usage clean.
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation Successful", data);
    }

    // Method for failure cases to standardize error handling.
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}