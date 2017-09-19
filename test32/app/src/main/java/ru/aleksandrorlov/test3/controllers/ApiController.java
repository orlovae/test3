package ru.aleksandrorlov.test3.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.aleksandrorlov.test3.rest.ApiUser;

/**
 * Created by alex on 05.09.17.
 */

public class ApiController {
    private static final String USER_BASE_URL = "https://bb-test-server.herokuapp.com/";

    static OkHttpClient client = new OkHttpClient.Builder().build();

    public static ApiUser API() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(USER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ApiUser.class);
    }
}
