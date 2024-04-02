package com.nagarro.javamini2.controller;

import com.nagarro.javamini2.util.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    // Default endpoint to handle non existing route request
    @RequestMapping("*")
    public void handleDefault() throws CustomException {
        throw new CustomException("Page not found", HttpStatus.NOT_FOUND.value());
    }
}
