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

public class FetchDiscoverMovies extends AsyncTask<Bundle, Void, ArrayList<ShowThumbnail>> {

    private ImageAdapter imageAdapter;

    public FetchDiscoverMovies(ImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    @Override
    protected ArrayList<ShowThumbnail> doInBackground(Bundle... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String showJsonString = null;

        try {

            final String MOVIEDB_AUTHORITY = "api.themoviedb.org";
            final String MOVIEDB_VERSION = "3";

            ////// PATHS //////
            final String DISCOVER_PATH = "discover";
            final String SHOW_TYPE_PATH = "movie";

            ////// PARAMS //////
            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";
            final String SORTING_PARAM = "sort_by";
            final String ADULT_PARAM = "include_adult";
            final String INCLUDE_VIDEO_PARAM = "include_video";
            final String PAGE_PARAM = "page";
            final String PRIMARY_RELEASE_YEAR_PARAM = "primary_release_year";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(MOVIEDB_AUTHORITY)
                    .appendPath(MOVIEDB_VERSION)
                    .appendPath(DISCOVER_PATH)
                    .appendPath(SHOW_TYPE_PATH)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                    .appendQueryParameter(SORTING_PARAM, params[0].getString("SORTING_PARAM"))
                    .appendQueryParameter(ADULT_PARAM, "false")
                    .appendQueryParameter(INCLUDE_VIDEO_PARAM, "false")
                    .appendQueryParameter(PAGE_PARAM, Integer.toString(params[0].getInt("PAGE_PARAM")));

            String primaryReleaseYear = params[0].getString("PRIMARY_RELEASE_YEAR_PARAM");
            if(primaryReleaseYear != null)
                builtUri.appendQueryParameter(PRIMARY_RELEASE_YEAR_PARAM, primaryReleaseYear);

            URL url = new URL(builtUri.build().toString());

            //2) Connect to the URL.
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
            //return null;
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
                String movieTitle = movieObject.getString("title");

                // 3) Get a complete link to the poster.

                String posterSubPath = movieObject.getString("poster_path");
                String posterFullPath = null;
                //Log.e("Default posterPath: ", posterPath);

                if (posterSubPath != "null") {
                    Uri.Builder builtUri = new Uri.Builder();
                    builtUri.scheme("https")
                            .encodedAuthority("image.tmdb.org")
                            .appendPath("t")
                            .appendPath("p")
                            .appendPath("w500")
                            .appendPath(posterSubPath.substring(1));

                    posterFullPath = builtUri.build().toString();
                }
                //Log.e("Build URL: ", moviePosterLink);

                // 4) Save values to the ArrayList.
                ShowThumbnail thumbnail = new ShowThumbnail(movieId, movieTitle, posterFullPath);
                showThumbnailsList.add(thumbnail);
            }
        } catch (JSONException e) {
            Log.e("Json Poster Exception: ", e.getMessage(), e);
        }

        return showThumbnailsList;
    }

    @Override
    protected void onPostExecute(ArrayList<ShowThumbnail> resultArray) {
        for (ShowThumbnail showItem : resultArray)
            imageAdapter.add(showItem);
    }
}
