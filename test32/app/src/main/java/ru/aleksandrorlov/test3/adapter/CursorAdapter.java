package ru.aleksandrorlov.test3.adapter;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.model.User;

/**
 * Created by alex on 05.09.17.
 */

public class CursorAdapter {
    private List<User> users;
    private User user;

    public List<User> getListToCursor(Cursor cursor){
        if (users == null) {
            users = new ArrayList<>();
        } else {
            users.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int idServerColIndex = cursor.getColumnIndex(Contract.User.COLUMN_ID_SERVER);
                int firstNameColIndex = cursor.getColumnIndex(Contract.User.COLUMN_FIRST_NAME);
                int lastNameColIndex = cursor.getColumnIndex(Contract.User.COLUMN_LAST_NAME);
                int emailCollIndex = cursor.getColumnIndex(Contract.User.COLUMN_EMAIL);
                int avatarURLColIndex = cursor.getColumnIndex(Contract.User.COLUMN_AVATAR_URL);
                int createAtColIndex = cursor.getColumnIndex(Contract.User.COLUMN_CREATE_AT);
                int updateAtColIndex = cursor.getColumnIndex(Contract.User.COLUMN_UPDATE_AT);

                do {
                    int idServerFromCursor = cursor.getInt(idServerColIndex);
                    String firstNameFromCursor = cursor.getString(firstNameColIndex);
                    String lastNameFromCursor = cursor.getString(lastNameColIndex);
                    String emailFromCursor = cursor.getString(emailCollIndex);
                    String avatarURLFromCursor = cursor.getString(avatarURLColIndex);
                    String createAtFromCursor = cursor.getString(createAtColIndex);
                    String updateAtFromCursor = cursor.getString(updateAtColIndex);

                    user = new User(idServerFromCursor, firstNameFromCursor, lastNameFromCursor,
                            emailFromCursor, avatarURLFromCursor, createAtFromCursor,
                            updateAtFromCursor);
                    users.add(user);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return users;
    }
}
