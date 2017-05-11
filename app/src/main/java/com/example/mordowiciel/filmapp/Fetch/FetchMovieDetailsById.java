package com.example.mordowiciel.filmapp.Fetch;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mordowiciel.filmapp.BuildConfig;
import com.example.mordowiciel.filmapp.Class.MovieDetails;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class FetchMovieDetailsById extends AsyncTask<String, Void, MovieDetails> {

    private MovieDetailsCallback mMovieDetailsCallback;

    public interface MovieDetailsCallback {
        void onAsyncExecutedPopulateView(MovieDetails movieDetails);
    }


    public FetchMovieDetailsById(Context ctx) {
        mMovieDetailsCallback = (MovieDetailsCallback) ctx;
    }

    @Override
    protected MovieDetails doInBackground(String... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieDetailsJsonString = null;

        try {

            // 1) Build URL.

            final String MOVIEDB_AUTHORITY = "api.themoviedb.org";
            final String MOVIEDB_VERSION = "3";
            final String SHOW_TYPE = "movie";
            final String MOVIE_ID = params[0];

            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(MOVIEDB_AUTHORITY)
                    .appendPath(MOVIEDB_VERSION)
                    .appendPath(SHOW_TYPE)
                    .appendPath(MOVIE_ID)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US");

            URL url = new URL(builtUri.build().toString());
            //Log.e("Url: ", url.toString());

            // 2) Connect to URL.

            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();

            // 3) Get data from input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            movieDetailsJsonString = buffer.toString();
            //Log.e("movieJsonString: ", movieDetailsJsonString);


        }
        catch (IOException e) {
            Log.e("FetchMovieDetailsById: ", e.getMessage());
        }
        finally {
            urlConnection.disconnect();
            try {
                reader.close();
            }
            catch (IOException e) {
                Log.e("Closing reader error: ", e.getMessage());
            }

        }

        // 4) Get data from JSON string.
        MovieDetails movieDetails = null;

        try {

            JSONObject jsonRoot = new JSONObject(movieDetailsJsonString);

            String id = jsonRoot.getString("id");
            String title = jsonRoot.getString("title");
            String originalTitle = jsonRoot.getString("original_title");
            String overview = jsonRoot.getString("overview");
            String releaseDate = jsonRoot.getString("release_date");
            double voteAverage = jsonRoot.getDouble("vote_average");
            String posterSubPath = jsonRoot.getString("poster_path");
            String posterFullPath = null;
            //Log.e("ID: ", id);
            //Log.e("Title: ", title);
            //Log.e("Original title: ", originalTitle);
            //Log.e("Release date: ", releaseDate);

            if (posterSubPath != "null") {
                Uri.Builder posterUri = new Uri.Builder();
                posterUri.scheme("https")
                        .encodedAuthority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w500")
                        // Using the substring to eliminate the first sign ("/") from the poster path.
                        // It is going to be appended by Uri.Builder.
                        .appendPath(posterSubPath.substring(1));

                posterFullPath = posterUri.build().toString();
            }
            Log.e("Movie poster link: ", "" + posterFullPath);

            if (overview.equals("null") || overview.equals(""))
                overview = "No overview found.";

            if (releaseDate.equals("null") || releaseDate.equals(""))
                releaseDate = "No info found.";

            // 5) Save values to returned MovieDetails.
            movieDetails = new MovieDetails(id, title, originalTitle, overview, releaseDate,
                    voteAverage, posterFullPath);

        }
        catch (JSONException e){
            Log.e("JSON parsing error: ", e.getMessage());
        }

        return movieDetails;
    }

    //Update fragment UI at the end of fetching data.
    @Override
    protected void onPostExecute(MovieDetails movieDetails) {

        mMovieDetailsCallback.onAsyncExecutedPopulateView(movieDetails);
    }

}
