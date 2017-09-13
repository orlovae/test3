package ru.aleksandrorlov.test3.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.aleksandrorlov.test3.rest.ApiUser;

/**
 * Created by alex on 05.09.17.
 */

public class ApiController {
    private static final String USER_BASE_URL = "https://bb-test-server.herokuapp.com/";

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor
            (message -> Log.d("Retrofit", message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor ).build();

    public static ApiUser getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(USER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ApiUser.class);
    }

    public static ApiUser setApi() {
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(USER_BASE_URL)
                .build();

        return retrofit.create(ApiUser.class);
    }
}
