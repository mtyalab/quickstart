package com.quickstart.util;

public class CustomResponses {

    public static StatusResponse statusResponse(String message, String statusCode) {
        return new StatusResponse(message, statusCode);
    }
 }
