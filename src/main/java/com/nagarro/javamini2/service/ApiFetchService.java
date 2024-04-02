package com.nagarro.javamini2.service;

import com.nagarro.javamini2.model.GenderApiResponse;
import com.nagarro.javamini2.model.NationalityApiResponse;
import com.nagarro.javamini2.model.UserApiResponse.UserApiResponse;

public interface ApiFetchService {

    public UserApiResponse getRandomUser();

    public NationalityApiResponse getNationalityByName(String name);

    public GenderApiResponse getGenderByName(String name);
}
