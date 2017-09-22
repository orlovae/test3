package ru.aleksandrorlov.test3;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.test3.adapter.CursorAdapter;
import ru.aleksandrorlov.test3.controllers.ApiController;
import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.rest.ApiUser;
import ru.aleksandrorlov.test3.utils.DownloadAvatar;

import static ru.aleksandrorlov.test3.model.User.COMPARE_BY_COUNT;

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
        apiUser = ApiController.API();
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
        Log.d("App","checkDataBase start");
        String[] projection = {Contract.User.COLUMN_ID_SERVER, Contract.User.COLUMN_FIRST_NAME,
                               Contract.User.COLUMN_LAST_NAME, Contract.User.COLUMN_EMAIL,
                               Contract.User.COLUMN_AVATAR_URL, Contract.User.COLUMN_CREATE_AT,
                               Contract.User.COLUMN_UPDATE_AT};

        Cursor cursor = getContentResolver().query(Contract.User.CONTENT_URI,
                projection, null, null, null);

        CursorAdapter cursorAdapter = new CursorAdapter();
        List<User> usersFromCursor = cursorAdapter.getListToCursor(cursor);
        Collections.sort(users, COMPARE_BY_COUNT);
        Collections.sort(usersFromCursor, COMPARE_BY_COUNT);

        if (!users.equals(usersFromCursor)){
            getContentResolver().delete(Contract.User.CONTENT_URI, null, null);
            createNewUserTable(users);
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d("App","checkDataBase end");
    }

    private void createNewUserTable(List<User> users){
        Log.d("App","createNewUserTable start");
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
        downloadAvatar();
        Log.d("App","createNewUserTable end");
    }

    private void downloadAvatar() {
        TreeMap<Integer, String> mapForDownloadAvatar = new TreeMap<>();

        String[] projection = {Contract.User.COLUMN_ID, Contract.User.COLUMN_AVATAR_URL};

        Cursor cursor = getContentResolver().query(Contract.User.CONTENT_URI,
                projection, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()){
                int idColIndex = cursor.getColumnIndex(Contract.User.COLUMN_ID);
                int avatarURLColIndex = cursor.getColumnIndex(Contract.User.COLUMN_AVATAR_URL);
                do {
                    Integer idFromCursor = cursor.getInt(idColIndex);
                    String avatarURLFromCursor = cursor.getString(avatarURLColIndex);
                    mapForDownloadAvatar.put(idFromCursor, avatarURLFromCursor);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        DownloadAvatar downloadAvatar = new DownloadAvatar(getApplicationContext(),
                mapForDownloadAvatar);
        downloadAvatar.execute();
    }
}
