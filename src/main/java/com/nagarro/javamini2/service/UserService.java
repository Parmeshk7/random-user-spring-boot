package com.nagarro.javamini2.service;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.UserApiResponse.UserApiResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {

    public List<User> createUsers(Integer size) throws ExecutionException, InterruptedException;
    public List<User> getSortedUsers(List<User> users, String sortType, String sortOrder);
    public List<User> getUsersWithLimitAndOffset(Integer limit, Integer offset);
    public Long getAllUsersCount();

}
