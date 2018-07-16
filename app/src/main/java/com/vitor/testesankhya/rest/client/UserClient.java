package com.vitor.testesankhya.rest.client;

import com.vitor.testesankhya.model.Page;
import com.vitor.testesankhya.rest.service.RetrofitConfig;
import com.vitor.testesankhya.rest.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserClient {

    private static final String TAG = "CEPClient";

    private static UserClient instance;
    private static UserService service;
    private boolean logJson = true;

    public UserClient() {
        instance = this;
        service = RetrofitConfig.getInstance().getRetrofit().create(UserService.class);
    }

    public static UserClient getInstance() {
        if (instance == null) {
            instance = new UserClient();
        }

        return instance;
    }

    public void doGetUserList(Integer page, final RetrofitConfig.OnRestResponseListener<Page> listener) {
        service.doGetUserList(page).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (response.isSuccessful()) {
                    listener.onRestSuccess(response.body());
                } else {
                    listener.onRestError(response.errorBody(), response.code());
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onRestError(null, null);
            }
        });
    }

}
