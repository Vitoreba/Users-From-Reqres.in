package com.vitor.testesankhya.rest.service;

import com.vitor.testesankhya.model.Page;
import com.vitor.testesankhya.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("/api/users?")
    Call<Page> doGetUserList(@Query("page") Integer page);

}
