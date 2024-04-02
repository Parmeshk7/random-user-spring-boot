package com.nagarro.javamini2.service.impl;

import com.nagarro.javamini2.model.GenderApiResponse;
import com.nagarro.javamini2.model.NationalityApiResponse;
import com.nagarro.javamini2.model.UserApiResponse.UserApiResponse;
import com.nagarro.javamini2.service.ApiFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiFetchServiceImpl implements ApiFetchService {

    private final WebClient userApiClient;
    private final WebClient nationalityApiClient;
    private final WebClient genderApiClient;

    @Autowired
    public ApiFetchServiceImpl(WebClient userApiClient, WebClient nationalityApiClient, WebClient genderApiClient) {
        this.userApiClient = userApiClient;
        this.nationalityApiClient = nationalityApiClient;
        this.genderApiClient = genderApiClient;
    }

    // Returns a Random User details
    @Override
    public UserApiResponse getRandomUser() {
        return userApiClient.get().retrieve().bodyToMono(UserApiResponse.class).block();
    }

    // Returns the nationality details of a user by The first name
    @Override
    public NationalityApiResponse getNationalityByName(String name) {
        return nationalityApiClient.get().uri("?name="+name).retrieve().bodyToMono(NationalityApiResponse.class).block();
    }

    // Returns the gender details of a user by the first name
    @Override
    public GenderApiResponse getGenderByName(String name) {
        return genderApiClient.get().uri("?name=" + name).retrieve().bodyToMono(GenderApiResponse.class).block();
    }
}
