package com.nagarro.javamini2.service.impl;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.SortOrder;
import com.nagarro.javamini2.service.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NameSortStrategy implements SortStrategy {

    private SortOrder sortOrder;    // Declaration of sort Order

    public NameSortStrategy(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override   // Implementation of sort function based on name length
    public List<User> sort(List<User> users) {
        if(Objects.equals(sortOrder, SortOrder.EVEN)){
            return users.stream().sorted(Comparator.comparingInt(user -> user.getName().length() % 2)).collect(Collectors.toList());
        }
        else{
            return users.stream().sorted(Comparator.comparingInt(user -> user.getName().length() % 2 == 1 ? 0 : 1)).collect(Collectors.toList());
        }
    }
}
