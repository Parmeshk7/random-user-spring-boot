package com.nagarro.javamini2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nagarro.javamini2.entity.User;
import com.nagarro.javamini2.model.SortOrder;
import com.nagarro.javamini2.model.SortType;
import com.nagarro.javamini2.service.UserService;
import com.nagarro.javamini2.util.CustomException;
import com.nagarro.javamini2.validator.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final ValidatorFactory validatorFactory = new ValidatorFactory();

    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    private UserController userController;

    private final ObjectWriter objectWriter = new ObjectMapper().writer();

    private final User user1 = newUser(1, "Samosa" ,64 , "male", "IN", "VERIFIED");
    private final User user2 = newUser(2, "Rasmalai", 59, "female", "IN", "VERIFIED");
    private final User user3 = newUser(3, "Gulab Jamun", 75, "male", "IN", "TO_BE_VERIFIED");
    private final User user4 = newUser(4, "Pizza", 26, "male", "IT", "TO_BE_VERIFIED");
    private final User user5 = newUser(5, "Pani Puri", 39, "female", "IN", "VERIFIED");


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.userController = new UserController(this.userService, this.validatorFactory);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    }


    @Nested
    class CreateUserEndpointTest {
        Map<String, String> requestBody = new HashMap<>();
        ResponseEntity<List<User>> responseEntity;


        @Test
        void testCreateUsersWithSize1() throws ExecutionException, InterruptedException, CustomException {
            Mockito.when(userService.createUsers(1)).thenReturn(List.of(user1));
            requestBody.put("size", "1");
            responseEntity = userController.createUsers(requestBody);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertEquals(1, responseEntity.getBody().size());
            assertEquals(1, responseEntity.getBody().get(0).getUserId());
        }

        @Test
        void testCreateUsersWithSize2() throws ExecutionException, InterruptedException, CustomException {
            Mockito.when(userService.createUsers(2)).thenReturn(List.of(user1, user2));

            requestBody.put("size", "2");
            responseEntity = userController.createUsers(requestBody);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertEquals(2, responseEntity.getBody().size());
            assertEquals(1, responseEntity.getBody().get(0).getUserId());
            assertEquals(2, responseEntity.getBody().get(1).getUserId());
        }

        @Test
        void testCreateUsersWithSize5() throws ExecutionException, InterruptedException, CustomException {
            Mockito.when(userService.createUsers(5)).thenReturn(List.of(user1, user2, user3, user4, user5));

            requestBody.put("size", "5");
            responseEntity = userController.createUsers(requestBody);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertEquals(5, responseEntity.getBody().size());
            assertEquals(1, responseEntity.getBody().get(0).getUserId());
            assertEquals(2, responseEntity.getBody().get(1).getUserId());
            assertEquals(3, responseEntity.getBody().get(2).getUserId());
            assertEquals(4, responseEntity.getBody().get(3).getUserId());
            assertEquals(5, responseEntity.getBody().get(4).getUserId());
        }

        @Test
        void testCreateUsersWithInvalidSize() throws Exception {
            requestBody.put("size", "6");
            assertThrows(CustomException.class, () -> userController.createUsers(requestBody),
                    "Size must be integer and can only between 1 to 5 (inclusive)");

            //Testing with size < 1
            requestBody.put("size", "0");
            assertThrows(CustomException.class, () -> userController.createUsers(requestBody),
                    "Size must be integer and can only between 1 to 5 (inclusive)");

            //Testing with invalid Size
            requestBody.put("size", "5d");
            assertThrows(CustomException.class, () -> userController.createUsers(requestBody),
                    "Size must be integer and can only between 1 to 5 (inclusive)");

            requestBody.put("size", "0");
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectWriter.writeValueAsString(requestBody));

            assertThrows(Exception.class, () -> {
                mockMvc.perform(mockRequest)
                        .andExpect(status().isBadRequest());

            }, "Size must be integer and can only between 1 to 5 (inclusive)");

        }

        @Test
        void testCreateUsersWithHttpPostRequest() throws Exception {
            Mockito.when(userService.createUsers(1)).thenReturn(List.of(user1));

            requestBody.put("size", "1");
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectWriter.writeValueAsString(requestBody));

            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].userId", is(1)));
        }

    }

    @Nested
    class GetUsersEndpointTest {
        List<User> users = Stream.of(user1, user2, user3, user4, user5).skip(1).limit(3).toList();
        ResponseEntity<Map<String, Object>> response;

        @Test
        void testGetUsers() throws CustomException {
            Mockito.when(userService.getUsersWithLimitAndOffset(3, 1))
                    .thenReturn(users);
            Mockito.when(userService.getSortedUsers(users, SortType.NAME.name(), SortOrder.ODD.name()))
                    .thenReturn(List.of(user3, user4, user2));
            Mockito.when(userService.getAllUsersCount()).thenReturn(5L);


            response = userController.getUsers(SortType.NAME.name(), SortOrder.ODD.name(), "3", "1");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            List<User> userList = (List<User>) response.getBody().get("data");
            assertEquals(3, userList.size());
            assertEquals(3, userList.get(0).getUserId());
            assertEquals(4, userList.get(1).getUserId());
            assertEquals(2, userList.get(2).getUserId());
        }

        @Test
        void testGetUsersHttpGetRequest() throws Exception {
            Mockito.when(userService.getUsersWithLimitAndOffset(3, 1))
                    .thenReturn(users);
            Mockito.when(userService.getSortedUsers(users, SortType.AGE.name(), SortOrder.EVEN.name()))
                    .thenReturn(List.of(user4, user2, user3));
            Mockito.when(userService.getAllUsersCount()).thenReturn(5L);


            mockMvc.perform(MockMvcRequestBuilders
                            .get("/users")
                            .param("sortType", SortType.AGE.name())
                            .param("sortOrder", SortOrder.EVEN.name())
                            .param("limit", "3")
                            .param("offset", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(3)))
                    .andExpect(jsonPath("$.data[0].userId", is(4)))
                    .andExpect(jsonPath("$.data[1].userId", is(2)))
                    .andExpect(jsonPath("$.data[2].userId", is(3)))
                    .andExpect(jsonPath("$.pageInfo.total", is(5)))
                    .andExpect(jsonPath("$.pageInfo.hasNextPage", is(true)))
                    .andExpect(jsonPath("$.pageInfo.hasPreviousPage", is(true)));
        }


        @Test
        void testGetUsersWithInvalidParams() {

            assertThrows(CustomException.class, () ->
                    userController.getUsers("name", "odd", "3", "-1"), "Offset must be a positive integer");


            assertThrows(Exception.class, () -> {
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/users")
                                .param("sortType", SortType.NAME.name())
                                .param("sortOrder", SortOrder.ODD.name())
                                .param("limit", "6")
                                .param("offset", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }, "Limit must be an integer in range 1 to 5 (inclusive)");

        }

    }


    private User newUser(Integer userId, String name, Integer age, String gender, String nationality, String verificationStatus){
        User user = new User();
        user.setUserId(Long.valueOf(userId));
        user.setName(name);
        user.setNationality(nationality);
        user.setGender(gender);
        user.setAge(age);
        user.setVerificationStatus(verificationStatus);
        return user;
    }
}