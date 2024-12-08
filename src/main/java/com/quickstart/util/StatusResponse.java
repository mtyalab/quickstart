package com.quickstart.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class StatusResponse {
    private String message;
    private String statusCode;
    private LocalDateTime timestamp;

    public StatusResponse(String message, String statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

}
