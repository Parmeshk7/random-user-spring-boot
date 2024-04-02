package com.nagarro.javamini2.service.impl;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.SortOrder;
import com.nagarro.javamini2.service.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AgeSortStrategy implements SortStrategy {

    private SortOrder sortOrder;        // Sort order declared as member variable

    public AgeSortStrategy(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override   // Implementation of sort function based on age
    public List<User> sort(List<User> users) {
        if(Objects.equals(sortOrder, SortOrder.EVEN)){
            return users.stream().sorted(Comparator.comparingInt(user -> user.getAge() % 2)).collect(Collectors.toList());
        }
        else if(Objects.equals(sortOrder, SortOrder.ODD)){
            return users.stream().sorted(Comparator.comparingInt(user -> user.getAge() % 2 == 1 ? 0 : 1)).collect(Collectors.toList());
        }
        return null;
    }
}
