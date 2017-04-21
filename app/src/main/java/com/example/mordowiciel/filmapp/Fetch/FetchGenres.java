package com.example.mordowiciel.filmapp.Fetch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mordowiciel.filmapp.BuildConfig;
import com.example.mordowiciel.filmapp.Database.DatabaseContract;
import com.example.mordowiciel.filmapp.Database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;


public class FetchGenres extends AsyncTask<Void, Void, Map<String, String>> {

    private DatabaseHelper mDatabaseHelper;

    public FetchGenres(Context ctx) {
        mDatabaseHelper = new DatabaseHelper(ctx);
    }

    @Override
    public Map<String, String> doInBackground(Void... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String genreJsonString = null;

        try {

            // 1) Build URL.
            final String MOVIEDB_AUTHORITY = "api.themoviedb.org";
            final String MOVIEDB_VERSION = "3";
            final String GENRE_PATH = "genre";
            final String SHOW_TYPE_PATH = "movie";
            final String LIST_PATH = "list";

            final String API_KEY_PARAM = "api_key";


            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(MOVIEDB_AUTHORITY)
                    .appendPath(MOVIEDB_VERSION)
                    .appendPath(GENRE_PATH)
                    .appendPath(SHOW_TYPE_PATH)
                    .appendPath(LIST_PATH)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY);

            URL url = new URL(builtUri.build().toString());

            // 2) Connect to the URL.
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();

            // 3) Get data from input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + '\n');
            }

            genreJsonString = buffer.toString();
            Log.v("GenresString: ", genreJsonString);

        } catch (IOException e) {
            Log.e("Error fetching genres: ", e.getMessage());

        } finally {

            urlConnection.disconnect();
            try {
                reader.close();
            } catch (IOException e) {
                Log.e("Closing reader:", e.getMessage(), e);
            }
        }

        // 4) Parse JSON string.
        Map<String, String> genresMap = new HashMap<>();
        try {
            JSONObject jsonRoot = new JSONObject(genreJsonString);
            JSONArray genreArray = jsonRoot.getJSONArray("genres");

            for (int i = 0; i < genreArray.length(); i++) {

                JSONObject genre = genreArray.getJSONObject(i);
                String _id = genre.getString("id");
                String name = genre.getString("name");

                genresMap.put(_id, name);
            }
        } catch (JSONException e) {
            Log.e("JSON Exception!", e.getMessage());
        }

        return genresMap;
    }


    @Override
    public void onPostExecute(Map<String, String> genresMap) {

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Set<String> mapKeys = genresMap.keySet();

        for (String key : mapKeys) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.GenreTable._ID, key);
            values.put(DatabaseContract.GenreTable.COLUMN_NAME, genresMap.get(key));
            Log.e("Genre: ", "ID: " + key + ", Name: " + genresMap.get(key));
            db.insert(DatabaseContract.GenreTable.TABLE_NAME, null, values);
        }

        db.close();
    }
}

