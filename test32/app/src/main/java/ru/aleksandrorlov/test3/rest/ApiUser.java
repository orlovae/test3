package ru.aleksandrorlov.test3.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.aleksandrorlov.test3.model.User;

/**
 * Created by alex on 05.09.17.
 */

public interface ApiUser {
    @GET("users.json")
    Call<List<User>> getUsers();
}
