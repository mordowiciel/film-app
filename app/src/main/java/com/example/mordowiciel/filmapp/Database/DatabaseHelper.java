package com.example.mordowiciel.filmapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mordowiciel.filmapp.Database.DatabaseContract.GenreTable;

/**
 * Created by mordowiciel on 20.04.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "moovieDatabase.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GenreTable.TABLE_NAME + " (" +
                    GenreTable._ID + " INTEGER PRIMARY KEY," +
                    GenreTable.COLUMN_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GenreTable.TABLE_NAME;

    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onUpgrade(database, oldVersion, newVersion);
    }
}
