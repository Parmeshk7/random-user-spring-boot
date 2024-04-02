package com.nagarro.javamini2.validator;

public class EnglishAlphabetsValidator implements Validator{

    private static EnglishAlphabetsValidator instance;
    private EnglishAlphabetsValidator() {}

    public static EnglishAlphabetsValidator getInstance() {
        // Checking that the object is already created or not for maintaining the singleton pattern
        if(instance == null){
            instance = new EnglishAlphabetsValidator();
        }
        return instance;
    }

    @Override
    public boolean validate(String parameter) {
        return parameter.matches("^[a-zA-Z]+$");
    }
}
