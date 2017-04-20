package com.example.mordowiciel.filmapp.Database;

import android.provider.BaseColumns;

/**
 * Created by mordowiciel on 20.04.17.
 */

public final class DatabaseContract {

    private DatabaseContract() {
    }

    public static class GenreTable implements BaseColumns {

        public static final String TABLE_NAME = "genres";
        public static final String COLUMN_NAME = "name";
    }

}
