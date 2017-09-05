package ru.aleksandrorlov.test3.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.aleksandrorlov.test3.data.Contract;

/**
 * Created by alex on 05.09.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Test3.db";
    public static final int TABLE_VERSION = 1;

    private final String SQL_CREATE_HAMSTER_TABLE = "CREATE TABLE "
            + Contract.User.TABLE_NAME + " ("
            + Contract.User.COLUMN_ID + " integer primary key autoincrement,"
            + Contract.User.COLUMN_ID_SERVER + " integer,"
            + Contract.User.COLUMN_FIRST_NAME + " text,"
            + Contract.User.COLUMN_LAST_NAME + " text,"
            + Contract.User.COLUMN_EMAIL + " text,"
            + Contract.User.COLUMN_AVATAR_URL + " text,"
            + Contract.User.COLUMN_CREATE_AT + " text,"
            + Contract.User.COLUMN_UPDATE_AT + " text);";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HAMSTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.User.TABLE_NAME);
        onCreate(db);
    }
}
