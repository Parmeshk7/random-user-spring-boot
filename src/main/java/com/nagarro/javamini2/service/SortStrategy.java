package com.nagarro.javamini2.service;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.SortOrder;

import java.util.List;

public interface SortStrategy {

    public List<User> sort(List<User> users);
}
