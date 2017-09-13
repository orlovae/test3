package ru.aleksandrorlov.test3.rest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.model.RequestBody;

/**
 * Created by alex on 05.09.17.
 */

public interface ApiUser {
    @GET("users.json")
    Call<List<User>> getUsers();

    @POST("users.json")
    Call<ResponseBody> setUser(@Body RequestBody requestBody);
}
