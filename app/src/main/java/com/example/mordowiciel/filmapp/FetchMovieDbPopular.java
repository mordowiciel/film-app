package com.example.mordowiciel.filmapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridView;

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

public class FetchMovieDbPopular extends AsyncTask<String, Void, ArrayList<Movie>> {

    ImageAdapter imageAdapter;

    FetchMovieDbPopular(ImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonString = null;

        try {

            //1) Built URL to connect.
            final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";
            final String SORTING_PARAM = "sort_by";
            final String ADULT_PARAM = "include_adult";
            final String INCLUDE_VIDEO_PARAM = "include_video";
            final String PAGE_PARAM = "1";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon().
                    appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY).
                    appendQueryParameter(LANGUAGE_PARAM, "en-US").
                    appendQueryParameter(SORTING_PARAM, params[0]).
                    appendQueryParameter(ADULT_PARAM, "true").
                    appendQueryParameter(INCLUDE_VIDEO_PARAM, "false").
                    appendQueryParameter(PAGE_PARAM, "1").
                    build();

            URL url = new URL(builtUri.toString());

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

        String movieId;
        String movieTitle;
        String movieOriginalTitle;
        String movieOverview;
        String movieReleaseDate;
        double movieVoteAverage;
        String moviePosterLink;
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        try {
            JSONObject jsonRoot = new JSONObject(movieJsonString);
            JSONArray resultsArray = jsonRoot.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                //Get a single movie object.
                JSONObject movieObject = resultsArray.getJSONObject(i);

                // 1) Get a movie ID.
                movieId = movieObject.getString("id");

                // 2) Get a movie title.
                movieTitle = movieObject.getString("title");

                // 3) Get a movie original title.
                movieOriginalTitle = movieObject.getString("original_title");

                // 4) Get a movie overview string.
                movieOverview = movieObject.getString("overview");

                // 5) Get a movie release date string.
                movieReleaseDate = movieObject.getString("release_date");

                // 6) Get a movie vote average.
                movieVoteAverage = movieObject.getDouble("vote_average");

                // 7) Get a complete link to the poster.

                String posterPath = movieObject.getString("poster_path");
                // TODO: Dlaczego nie dziala UriBuilder?
                /*
                Uri.Builder builtUri = new Uri.Builder();
                builtUri.scheme("https")
                        .authority(MOVIEDB_MAIN_IMAGE_URL)
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w500")
                        .appendPath(posterPath);
                moviePosterLink = builtUri.build().toString();
                */

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("https://image.tmdb.org/t/p/w500");
                stringBuilder.append(posterPath);

                moviePosterLink = stringBuilder.toString();
                Log.e("stringBuilderLink:", moviePosterLink);

                // 5) Save values to the ArrayList.
                Movie movie = new Movie(movieId, movieTitle, movieOriginalTitle, movieOverview,
                        movieReleaseDate, movieVoteAverage, moviePosterLink);
                movieList.add(movie);
            }
        } catch (JSONException e) {
            Log.e("Json Poster Exception: ", e.getMessage(), e);
        }

        return movieList;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> resultArray) {

        if (resultArray != null) {
            imageAdapter.clear();
            for (Movie movieItem : resultArray)
                imageAdapter.add(movieItem);
        }

    }
}
