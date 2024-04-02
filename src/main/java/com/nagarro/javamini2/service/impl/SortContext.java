package com.nagarro.javamini2.service.impl;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.SortOrder;
import com.nagarro.javamini2.service.SortStrategy;

import java.util.List;

public class SortContext {

    private SortStrategy strategy;

    public SortContext(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public List<User> sort(List<User> users) {
        return strategy.sort(users);
    }

}
