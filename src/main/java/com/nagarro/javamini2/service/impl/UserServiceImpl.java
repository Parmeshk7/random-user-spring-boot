package com.nagarro.javamini2.service.impl;

import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.GenderApiResponse;
import com.nagarro.javamini2.model.NationalityApiResponse;
import com.nagarro.javamini2.model.SortOrder;
import com.nagarro.javamini2.model.SortType;
import com.nagarro.javamini2.model.UserApiResponse.UserResult;
import com.nagarro.javamini2.repository.UserRepository;
import com.nagarro.javamini2.service.ApiFetchService;
import com.nagarro.javamini2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApiFetchService apiFetchService;

    // Creating user based on size passed
    @Override
    public List<User> createUsers(Integer size) throws ExecutionException, InterruptedException {

        List<User> users = new ArrayList<>();
        for(int i=0; i<size; i++){      // Iterating for creating users based on size
            UserResult result = apiFetchService.getRandomUser().getResults().get(0);
            String verificationStatus = getVerificationStatus(result); // Calculating verification status
            User user = new User();
            user.setAge(result.getDob().getAge());
            user.setName(result.getName().getFirst() + " " + result.getName().getLast());
            user.setGender(result.getGender());
            user.setDob(result.getDob().getDate());
            user.setNationality(result.getNationality());
            user.setVerificationStatus(verificationStatus);
            user.setDateModified(new Date());
            user.setDateCreated(new Date());
            users.add(user);
        }
        return userRepository.saveAll(users);
    }

    // Function to validate the user based on nationality and gender
    private String getVerificationStatus(UserResult result) throws ExecutionException, InterruptedException {
        String name = result.getName().getFirst();
        String nationality = result.getNationality();

        //Creating a fixed thread pool for executing both the fetching operations concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Requesting a future objects that will be resolved
        Future<NationalityApiResponse> nationalityFuture = executorService.submit(() ->
                apiFetchService
                        .getNationalityByName(name));

        Future<GenderApiResponse> genderFuture = executorService.submit(() ->
                apiFetchService
                        .getGenderByName(name));

        // Retrieving the fetched results
        NationalityApiResponse nationalityApiResponse = nationalityFuture.get();
        GenderApiResponse genderApiResponse = genderFuture.get();

        // Shutting down both threads
        executorService.shutdown();

        // Checking the verification status
        if((nationalityApiResponse.getCountry().stream().anyMatch((country) -> country.get("country_id").toString().equalsIgnoreCase(nationality)))
            && (genderApiResponse.getGender().equalsIgnoreCase(result.getGender()))
        ){
            return "VERIFIED";
        }
        else {
            return "TO_BE_VERIFIED";
        }

    }

    // Function to get sorted users based on sort Type and sort Order
    @Override
    public List<User> getSortedUsers(List<User> users, String sortType, String sortOrder) {

        SortContext sortContext = null;
        if(Objects.equals(SortType.AGE, SortType.valueOf(sortType.toUpperCase()))){
              sortContext = new SortContext(new AgeSortStrategy(SortOrder.valueOf(sortOrder.toUpperCase())));
        }
        else if(Objects.equals(SortType.NAME, SortType.valueOf(sortType.toUpperCase()))){
            sortContext = new SortContext(new NameSortStrategy(SortOrder.valueOf(sortOrder.toUpperCase())));
        }
        return sortContext.sort(users);
    }


    // returns list of users based on the limit and offset
    @Override
    public List<User> getUsersWithLimitAndOffset(Integer limit, Integer offset) {
        return userRepository.findAll().stream().skip(offset).limit(limit).collect(Collectors.toList());
    }


    @Override
    public Long getAllUsersCount() {
        return userRepository.count();
    }
}
