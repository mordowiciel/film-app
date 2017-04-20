package com.example.mordowiciel.filmapp.Fetch;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.mordowiciel.filmapp.BuildConfig;
import com.example.mordowiciel.filmapp.Class.ImageAdapter;
import com.example.mordowiciel.filmapp.Class.ShowThumbnail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mordowiciel on 22.02.17.
 */

public class FetchDiscoverTv extends AsyncTask<Bundle, Void, ArrayList<ShowThumbnail>> {

    private ImageAdapter imageAdapter;

    public FetchDiscoverTv(ImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    @Override
    public ArrayList<ShowThumbnail> doInBackground (Bundle... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String showJsonString = null;

        try {

            // 1) Build URL.
            final String MOVIEDB_AUTHORITY = "api.themoviedb.org";
            final String MOVIEDB_VERSION = "3";
            final String DISCOVER_PATH = "discover";
            final String SHOW_TYPE_PATH = "tv";
            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";
            final String SORTING_PARAM = "sort_by";
            final String PAGE_PARAM = "page";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(MOVIEDB_AUTHORITY)
                    .appendPath(MOVIEDB_VERSION)
                    .appendPath(DISCOVER_PATH)
                    .appendPath(SHOW_TYPE_PATH)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                    .appendQueryParameter(SORTING_PARAM, params[0].getString("SORTING_PARAM"))
                    .appendQueryParameter(PAGE_PARAM, Integer.toString(params[0].getInt("PAGE_PARAM")));

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

            showJsonString = buffer.toString();
            Log.v("movieJsonString: ", showJsonString);

        } catch (IOException e) {
            Log.e("Download exception: ", e.getMessage(), e);

        } finally {

            urlConnection.disconnect();
            try {
                reader.close();
            } catch (IOException e) {
                Log.e("Closing reader:", e.getMessage(), e);
            }
        }

        // 4) Get data from JSON. (TODO: move to another class?)
        ArrayList<ShowThumbnail> showThumbnailsList = new ArrayList<ShowThumbnail>();
        try {
            JSONObject jsonRoot = new JSONObject(showJsonString);
            JSONArray resultsArray = jsonRoot.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                //Get a single movie object.
                JSONObject movieObject = resultsArray.getJSONObject(i);

                // 1) Get a movie ID.
                String movieId = movieObject.getString("id");

                // 2) Get a movie title.
                String movieTitle = movieObject.getString("name");

                // 3) Get a complete link to the poster.

                String posterPath = movieObject.getString("poster_path");
                posterPath = posterPath.substring(1);
                //Log.e("Default posterPath: ", posterPath);

                Uri.Builder builtUri = new Uri.Builder();
                builtUri.scheme("https")
                        .encodedAuthority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w500")
                        .appendPath(posterPath);
                String moviePosterLink = builtUri.build().toString();
                //Log.e("Build URL: ", moviePosterLink);

                // 4) Save values to the ArrayList.
                ShowThumbnail thumbnail = new ShowThumbnail(movieId, movieTitle, moviePosterLink);
                showThumbnailsList.add(thumbnail);
            }
        } catch (JSONException e) {
            Log.e("Json Poster Exception: ", e.getMessage(), e);
        }

        return showThumbnailsList;
    }

    @Override
    public void onPostExecute (ArrayList<ShowThumbnail> resultArray) {
        for (ShowThumbnail showItem : resultArray){
            imageAdapter.add(showItem);
        }
    }
}
