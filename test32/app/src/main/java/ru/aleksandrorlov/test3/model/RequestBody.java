package ru.aleksandrorlov.test3.model;

/**
 * Created by alex on 13.09.17.
 */

public class RequestBody {
    private User user;

    public RequestBody(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
