package com.nagarro.javamini2.util;

import java.util.Date;

public class CustomException extends Exception{

    private String message;
    private Integer code;
    public CustomException(String message, Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
