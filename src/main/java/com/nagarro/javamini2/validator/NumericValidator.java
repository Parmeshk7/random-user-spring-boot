package com.nagarro.javamini2.validator;

public class NumericValidator implements Validator{

    private static NumericValidator instance;

    private NumericValidator() {}

    public static NumericValidator getInstance() {
        // Checking that the object is already created or not for maintaining the singleton pattern
        if(instance == null){
            instance = new NumericValidator();
        }
        return instance;
    }

    @Override
    public boolean validate(String parameter) {
        try{
            Integer.parseInt(parameter);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }
}
