package com.nagarro.javamini2.model.UserApiResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResult {

    @JsonProperty("gender")
    String gender;
    @JsonProperty("name")
    UserFullName name;
    @JsonProperty("email")
    String email;
    @JsonProperty("dob")
    UserDob dob;
    @JsonProperty("nat")
    String nationality;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserFullName getName() {
        return name;
    }

    public void setName(UserFullName name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDob getDob() {
        return dob;
    }

    public void setDob(UserDob dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
