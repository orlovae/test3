package ru.aleksandrorlov.test3.rest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.model.RequestBody;

/**
 * Created by alex on 05.09.17.
 */

public interface ApiUser {
    @GET("users.json")
    Call<List<User>> getUsers();

    @POST("users.json")
    Call<User> setUser(@Body RequestBody requestBody);

    @PATCH("users/{id}.json")
    Call<User> editUser(@Path("id") int idServer, @Body RequestBody requestBody);
}
