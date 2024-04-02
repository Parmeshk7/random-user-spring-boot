package com.nagarro.javamini2.model;

import java.util.List;
import java.util.Map;

public class NationalityApiResponse {

    private Long count;
    private String name;
    private List<Map<String, Object>> country;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getCountry() {
        return country;
    }

    public void setCountry(List<Map<String, Object>> country) {
        this.country = country;
    }
}
