package com.nagarro.javamini2.validator;

import com.nagarro.javamini2.model.ValidatorType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidatorFactory {

    public Validator getValidator(ValidatorType parameterType){

        // Returns Validator Object based on the type
        if(parameterType.equals(ValidatorType.NUMERIC)){
            return NumericValidator.getInstance();
        } else if (parameterType.equals(ValidatorType.ALPHABETICAL)) {
            return EnglishAlphabetsValidator.getInstance();
        }
        else {
            throw new IllegalArgumentException("Unsupported parameter type");
        }
    }
}
