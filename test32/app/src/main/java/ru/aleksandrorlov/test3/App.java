package ru.aleksandrorlov.test3;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.test3.adapter.CursorAdapter;
import ru.aleksandrorlov.test3.controllers.ApiController;
import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.rest.ApiUser;

/**
 * Created by alex on 05.09.17.
 */

public class App extends Application {
    private ApiUser apiUser;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        apiUser = ApiController.getApi();
        apiUser.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                try {
                    if (response.isSuccessful()){
                        List<User> users = response.body();
                        checkDataBase(users);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void checkDataBase (List<User> users){
        String[] projection = {Contract.User.COLUMN_ID_SERVER, Contract.User.COLUMN_FIRST_NAME,
                               Contract.User.COLUMN_LAST_NAME, Contract.User.COLUMN_EMAIL,
                               Contract.User.COLUMN_AVATAR_URL, Contract.User.COLUMN_CREATE_AT,
                               Contract.User.COLUMN_UPDATE_AT};

        Cursor cursor = getContentResolver().query(Contract.User.CONTENT_URI,
                projection, null, null, null);

        CursorAdapter cursorAdapter = new CursorAdapter();
        List<User> usersFromCursor = cursorAdapter.getListToCursor(cursor);

        if (!users.equals(usersFromCursor)){
            getContentResolver().delete(Contract.User.CONTENT_URI, null, null);
            createNewUserTable(users);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void createNewUserTable(List<User> users){
        for (User item:users
                ) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.User.COLUMN_ID_SERVER, item.getId());
            cv.put(Contract.User.COLUMN_FIRST_NAME, item.getFirstName());
            cv.put(Contract.User.COLUMN_LAST_NAME, item.getLastName());
            cv.put(Contract.User.COLUMN_EMAIL, item.getEmail());
            cv.put(Contract.User.COLUMN_AVATAR_URL, item.getAvatarUrl());
            cv.put(Contract.User.COLUMN_CREATE_AT, item.getCreatedAt());
            cv.put(Contract.User.COLUMN_UPDATE_AT, item.getUpdatedAt());
            getContentResolver().insert(Contract.User.CONTENT_URI, cv);
        }
//        downloadImage();
    }
}
