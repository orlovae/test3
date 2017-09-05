package ru.aleksandrorlov.test3.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.helper.DBHelper;

import static ru.aleksandrorlov.test3.data.Contract.AUTHORITY;
import static ru.aleksandrorlov.test3.data.Contract.User.PATH_USER;
import static ru.aleksandrorlov.test3.data.Contract.User.TYPE_USER_ALL_ROW;
import static ru.aleksandrorlov.test3.data.Contract.User.TYPE_USER_SINGLE_ROW;

/**
 * Created by alex on 05.09.17.
 */

public class DataBaseProvider extends ContentProvider {
    private static final int URI_MATCHER_USER_ALL_ROWS = 1000;
    private static final int URI_MATCHER_USER_SINGLE_ROW = 1001;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_USER, URI_MATCHER_USER_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_USER + "/#", URI_MATCHER_USER_SINGLE_ROW);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public void openDatabase () throws SQLiteException {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = dbHelper.getReadableDatabase();
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        String rowIdUser;
        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_USER_ALL_ROWS:
                table_name = Contract.User.TABLE_NAME;
                break;
            case URI_MATCHER_USER_SINGLE_ROW:
                table_name = Contract.User.TABLE_NAME;
                rowIdUser = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.User.COLUMN_ID + " = " + rowIdUser;
                } else {
                    selection = selection + " AND " + Contract.User.COLUMN_ID + " = " +
                            rowIdUser;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        Cursor cursor = database.query(table_name, projection, selection,
                selectionArgs, null, null, sortOrder);

        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {

            case URI_MATCHER_USER_ALL_ROWS:
                return TYPE_USER_ALL_ROW;
            case URI_MATCHER_USER_SINGLE_ROW:
                return TYPE_USER_SINGLE_ROW;
            default:
                throwIllegalArgumentException(uri);
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowIdUser;
        String table_name = "";
        Uri resultUri = null;

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_USER_ALL_ROWS:
                table_name = Contract.User.TABLE_NAME;
                rowIdUser = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIdUser);
                break;
            case URI_MATCHER_USER_SINGLE_ROW:
                table_name = Contract.User.TABLE_NAME;
                rowIdUser = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIdUser);
        }

        try {
            getContext().getContentResolver().notifyChange(resultUri, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String rowIdUser;
        int countRowsDelete = -1;
        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_USER_ALL_ROWS:
                table_name = Contract.User.TABLE_NAME;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_USER_SINGLE_ROW:
                table_name = Contract.User.TABLE_NAME;
                rowIdUser = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.User.COLUMN_ID + " = " + rowIdUser;
                } else {
                    selection = selection + " AND " + Contract.User.COLUMN_ID + " = "
                            + rowIdUser;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return countRowsDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        String rowIdUser;
        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_USER_ALL_ROWS:
                table_name = Contract.User.TABLE_NAME;
                break;
            case URI_MATCHER_USER_SINGLE_ROW:
                table_name = Contract.User.TABLE_NAME;
                rowIdUser = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.User.COLUMN_ID + " = " + rowIdUser;
                } else {
                    selection = selection + " AND " + Contract.User.COLUMN_ID + " = "
                            + rowIdUser;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }
        int countRowsUpdate = database.update(table_name, values, selection, selectionArgs);

        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return countRowsUpdate;
    }

    private void throwIllegalArgumentException (Uri uri){
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
}
