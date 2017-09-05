package ru.aleksandrorlov.test3.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 05.09.17.
 */

public final class Contract {

    public Contract() {
    }

    // Uri authority
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "ru.aleksandrorlov.test3";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    // path
    public static final class User implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_ID_SERVER = "id_server";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_AVATAR_URL = "avatar_url";
        public static final String COLUMN_CREATE_AT = "created_at";
        public static final String COLUMN_UPDATE_AT = "updated_at";

        public static final String PATH_USER = TABLE_NAME;

        // Общий Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);

        // Типы данных
        // набор строк //
        public static final String TYPE_HAMSTER_ALL_ROW = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + AUTHORITY + "/" + TABLE_NAME;

        // одна строка
        public static final String TYPE_HAMSTER_SINGLE_ROW = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + AUTHORITY + "/" + TABLE_NAME;
    }
}
