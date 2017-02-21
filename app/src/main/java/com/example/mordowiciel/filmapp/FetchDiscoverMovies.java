package com.example.mordowiciel.filmapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

public class FetchDiscoverMovies extends AsyncTask<FetchMoviesPassedParam, Void, ArrayList<MovieClass>> {

    ImageAdapter imageAdapter;

    FetchDiscoverMovies(ImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    @Override
    protected ArrayList<MovieClass> doInBackground(FetchMoviesPassedParam... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonString = null;

        try {

            final String MOVIEDB_AUTHORITY = "api.themoviedb.org";
            final String MOVIEDB_VERSION = "3";
            final String DISCOVER_PATH = "discover";
            final String SHOW_TYPE = "movie";
            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";
            final String SORTING_PARAM = "sort_by";
            final String ADULT_PARAM = "include_adult";
            final String INCLUDE_VIDEO_PARAM = "include_video";
            final String PAGE_PARAM = "page";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(MOVIEDB_AUTHORITY)
                    .appendPath(MOVIEDB_VERSION)
                    .appendPath(DISCOVER_PATH)
                    .appendPath(SHOW_TYPE)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                    .appendQueryParameter(SORTING_PARAM, params[0].getSorting())
                    .appendQueryParameter(ADULT_PARAM, "true")
                    .appendQueryParameter(INCLUDE_VIDEO_PARAM, "false")
                    .appendQueryParameter(PAGE_PARAM, Integer.toString(params[0].getPageNumber()));

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

            movieJsonString = buffer.toString();
            Log.v("movieJsonString: ", movieJsonString);

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

        ArrayList<MovieClass> movieList = new ArrayList<MovieClass>();
        try {
            JSONObject jsonRoot = new JSONObject(movieJsonString);
            JSONArray resultsArray = jsonRoot.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                //Get a single movie object.
                JSONObject movieObject = resultsArray.getJSONObject(i);

                // 1) Get a movie ID.
                String movieId = movieObject.getString("id");

                // 2) Get a movie title.
                String movieTitle = movieObject.getString("title");

                // 3) Get a movie original title.
                String movieOriginalTitle = movieObject.getString("original_title");

                // 4) Get a movie overview string.
                String movieOverview = movieObject.getString("overview");

                // 5) Get a movie release date string.
                String movieReleaseDate = movieObject.getString("release_date");

                // 6) Get a movie vote average.
                double movieVoteAverage = movieObject.getDouble("vote_average");

                // 7) Get a complete link to the poster.

                String posterPath = movieObject.getString("poster_path");
                posterPath = posterPath.substring(1);
                Log.e("Default posterPath: ", posterPath);

                Uri.Builder builtUri = new Uri.Builder();
                builtUri.scheme("https")
                        .encodedAuthority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w500")
                        .appendPath(posterPath);
                String moviePosterLink = builtUri.build().toString();
                Log.e("Build URL: ", moviePosterLink);

                // 8) Save values to the ArrayList.
                MovieClass movie = new MovieClass(movieId, movieTitle, movieOriginalTitle, movieOverview,
                        movieReleaseDate, movieVoteAverage, moviePosterLink);
                movieList.add(movie);
            }
        } catch (JSONException e) {
            Log.e("Json Poster Exception: ", e.getMessage(), e);
        }

        return movieList;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieClass> resultArray) {
        for (MovieClass movieItem : resultArray)
            imageAdapter.add(movieItem);
    }
}
