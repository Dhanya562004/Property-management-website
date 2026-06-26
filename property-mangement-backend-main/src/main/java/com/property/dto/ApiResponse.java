package com.property.dto;

import java.util.HashMap;

public class ApiResponse {
    private Integer responseCode;
    private String responseMessage;
    private Object responseData;

    public ApiResponse() {
        this.responseData = new HashMap<>();
    }

    public ApiResponse(Integer responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseData = new HashMap<>();
    }

    public ApiResponse(Integer responseCode, String responseMessage, Object responseData) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseData = responseData != null ? responseData : new HashMap<>();
    }

    // Success response helpers
    public static ApiResponse success() {
        return new ApiResponse(200, "Everything worked as expected");
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "Everything worked as expected", data);
    }

    // Error response helpers
    public static ApiResponse requiredParam(String paramName) {
        return new ApiResponse(201, paramName + " is required parameter!");
    }

    public static ApiResponse invalidInput(String paramName) {
        return new ApiResponse(202, paramName + " is invalid input format");
    }

    public static ApiResponse notMatch(String details) {
        return new ApiResponse(203, details + " is not match");
    }

    public static ApiResponse alreadyExist(String details) {
        return new ApiResponse(204, details + " is already exist");
    }

    public static ApiResponse noAccess() {
        return new ApiResponse(205, "You didn't have permission to access this API");
    }

    public static ApiResponse needToVerify() {
        return new ApiResponse(206, "Your account need to verification & It's takes some time");
    }

    public static ApiResponse readyToBookOnly() {
        return new ApiResponse(208, "Only Ready to Buy Properties can Book!");
    }

    public static ApiResponse tokenRequired() {
        return new ApiResponse(400, "Authentication token is required");
    }

    public static ApiResponse invalidToken() {
        return new ApiResponse(401, "Authentication token is invalid");
    }

    public static ApiResponse unknownError() {
        return new ApiResponse(500, "Something went wrong!");
    }

    public static ApiResponse customError(Integer code, String msg) {
        return new ApiResponse(code, msg);
    }

    // Getters and Setters
    public Integer getResponseCode() { return responseCode; }
    public void setResponseCode(Integer responseCode) { this.responseCode = responseCode; }

    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }

    public Object getResponseData() { return responseData; }
    public void setResponseData(Object responseData) { this.responseData = responseData; }
}
