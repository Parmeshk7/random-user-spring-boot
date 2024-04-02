package com.nagarro.javamini2.model;

import java.util.Date;

public class ErrorResponse {

    private String message;
    private Integer code;
    private Date timestamp;

    public ErrorResponse(String message, Integer code, Date timestamp) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }


    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
