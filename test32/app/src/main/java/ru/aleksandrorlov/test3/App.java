package ru.aleksandrorlov.test3;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        ApiUser apiUser = ApiController.API();
        apiUser.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                try {
                    if (response.isSuccessful()){
                        List<User> usersFromServer = response.body();
                        checkDataBase(usersFromServer);
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

    public void checkDataBase (List<User> usersFromServer){
        Log.d("App", "checkDataBase start");
        String[] projection = {Contract.User.COLUMN_ID_SERVER, Contract.User.COLUMN_FIRST_NAME,
                               Contract.User.COLUMN_LAST_NAME, Contract.User.COLUMN_EMAIL,
                               Contract.User.COLUMN_AVATAR_URL, Contract.User.COLUMN_CREATE_AT,
                               Contract.User.COLUMN_UPDATE_AT};

        Cursor cursor = getContentResolver().query(Contract.User.CONTENT_URI,
                projection, null, null, null);

        CursorAdapter cursorAdapter = new CursorAdapter();
        List<User> usersFromDB = cursorAdapter.getListToCursor(cursor);

        Collections.sort(usersFromServer, User.COMPARE_BY_ID_SERVER);
        Collections.sort(usersFromDB, User.COMPARE_BY_ID_SERVER);


        if (usersFromDB.isEmpty()){
            createNewUserTable(usersFromServer);
        } else {
            if (!usersFromServer.equals(usersFromDB)) {

            Log.d("App", "usersFromServer size = "
                    + usersFromServer.size()
                    + ". usersFromDB size = "
                    + usersFromDB.size());
            synchronizedDB(usersFromServer);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void createNewUserTable(List<User> usersFromServer){
        Log.d("App", "createNewUserTable start");
        for (User item:usersFromServer
                ) {
            getContentResolver().insert(Contract.User.CONTENT_URI, createCV(item));
        }
    }

    private void synchronizedDB(List<User> usersFromServer) {
        Log.d("App", "synchronizedDB start");
        int idServerFromServer;
        Date updatedAtFromServer = null;
        Date updatedAtFromDB;
        String selection = Contract.User.COLUMN_ID_SERVER + " LIKE ?";

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (User item:usersFromServer
             ) {
            idServerFromServer = item.getId();

            try {
                updatedAtFromServer = format.parse(item.getUpdatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String[] selectionArgs = {"" + idServerFromServer};

            Cursor cursor = getContentResolver().query(Contract.User.CONTENT_URI, null,
                    selection, selectionArgs, null);
            Log.d("App", "idServer = " + idServerFromServer + ", cursor is null - " + (cursor == null));

            try {
                if (cursor != null  && cursor.moveToFirst()){
                    int updateATColIndex = cursor.getColumnIndex(Contract.User.COLUMN_UPDATE_AT);
                    updatedAtFromDB = format.parse(cursor.getString(updateATColIndex));

                    if (updatedAtFromServer.after(updatedAtFromDB)) {
                        updateUserFromDB(idServerFromServer, item);
                    }
                } else {
                    insertNewUserToDB(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void insertNewUserToDB(User user) {
        Log.d("App", "insertNewUserToDB start");
        ContentValues cv = createCV(user);
        getContentResolver().insert(Contract.User.CONTENT_URI, cv);
    }

    private void updateUserFromDB(int idServerFromServer, User user) {
        Log.d("App", "updateUserFromDB start");
        ContentValues cv = createCV(user);

        String selection = Contract.User.COLUMN_ID_SERVER + " = ?";
        String[] selectionArgs = {"" + idServerFromServer};

        getContentResolver().update(Contract.User.CONTENT_URI, cv, selection,
                selectionArgs);
    }

    private ContentValues createCV(User user) {
        ContentValues cv = new ContentValues();

        cv.put(Contract.User.COLUMN_ID_SERVER, user.getId());
        cv.put(Contract.User.COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(Contract.User.COLUMN_LAST_NAME, user.getLastName());
        cv.put(Contract.User.COLUMN_EMAIL, user.getEmail());
        cv.put(Contract.User.COLUMN_AVATAR_URL, user.getAvatarUrl());
        cv.put(Contract.User.COLUMN_CREATE_AT, user.getCreatedAt());
        cv.put(Contract.User.COLUMN_UPDATE_AT, user.getUpdatedAt());
        return cv;
    }
}
