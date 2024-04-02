package com.nagarro.javamini2.controller;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.PageInfo;
import com.nagarro.javamini2.model.SortOrder;
import com.nagarro.javamini2.model.SortType;
import com.nagarro.javamini2.model.ValidatorType;
import com.nagarro.javamini2.util.CustomException;
import com.nagarro.javamini2.service.UserService;
import com.nagarro.javamini2.validator.Validator;
import com.nagarro.javamini2.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {
    private final UserService userService;
    private final ValidatorFactory validatorFactory;

    @Autowired
    public UserController(UserService userService, ValidatorFactory validatorFactory) {
        this.userService = userService;
        this.validatorFactory = validatorFactory;
    }

    // Endpoint for creating users based on the size attribute passed in request payload
    @PostMapping("/users")
    public ResponseEntity<List<User>> createUsers(@RequestBody Map<String, String> requestBody) throws ExecutionException, InterruptedException, CustomException {
        String size = requestBody.get("size"); //Fetching the size attribute from request payload

        // Checking for Invalid size
        if(!validatorFactory.getValidator(ValidatorType.NUMERIC).validate(size) || Integer.parseInt(size) < 1 || Integer.parseInt(size) > 5){
            throw new CustomException("Size must be integer and can only between 1 to 5 (inclusive)", HttpStatus.BAD_REQUEST.value());
        }
        return new ResponseEntity<>(userService.createUsers(Integer.parseInt(size)), HttpStatus.CREATED);
    }


    // Endpoint for fetching users based on the Query Params passed
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam String sortType, @RequestParam String sortOrder,
                           @RequestParam(defaultValue = "5") String limit, @RequestParam(defaultValue = "0") String offset) throws CustomException {


        validateListUsersParams(sortType, sortOrder, limit, offset);

        List<User> users = userService.getUsersWithLimitAndOffset(Integer.parseInt(limit), Integer.parseInt(offset));
        List<User> sortedUsers = userService.getSortedUsers(users, sortType, sortOrder);
        Long total = userService.getAllUsersCount();

        //Creating Page Info data
        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasPreviousPage(Integer.parseInt(offset) > 0 && total > 0);
        pageInfo.setHasNextPage((Integer.parseInt(offset) + sortedUsers.size()) < total);
        pageInfo.setTotal(total);

        // Generating the desired response object structure
        Map<String, Object> response = new HashMap<>();
        response.put("data", sortedUsers);
        response.put("pageInfo", pageInfo);
        return ResponseEntity.ok(response);
    }

    // Validator function for validating the params
    private void validateListUsersParams(String sortType, String sortOrder, String limit, String offset) throws CustomException {

        // Using Validators
        Validator numericValidator = validatorFactory.getValidator(ValidatorType.NUMERIC);
        Validator alphabeticalValidator = validatorFactory.getValidator(ValidatorType.ALPHABETICAL);

        if(!alphabeticalValidator.validate(sortType) ||
                !(SortType.NAME.name().equalsIgnoreCase(sortType) ||
                        SortType.AGE.name().equalsIgnoreCase(sortType))){
            throw new CustomException("Sort Type must be 'age' or 'name'", HttpStatus.BAD_REQUEST.value());
        }
        if(!alphabeticalValidator.validate(sortOrder) ||
                !(SortOrder.ODD.name().equalsIgnoreCase(sortOrder) ||
                        SortOrder.EVEN.name().equalsIgnoreCase(sortOrder))){
            throw new CustomException("Sort Order must be 'even' or 'odd'", HttpStatus.BAD_REQUEST.value());
        }
        if(!numericValidator.validate(limit) || Integer.parseInt(limit) < 1 || Integer.parseInt(limit) > 5){
            throw new CustomException("Limit must be an integer in range 1 to 5 (inclusive)", HttpStatus.BAD_REQUEST.value());
        }
        if(!numericValidator.validate(offset) || Integer.parseInt(offset) < 0){
            throw new CustomException("Offset must be a positive integer", HttpStatus.BAD_REQUEST.value());
        }
    }
}
