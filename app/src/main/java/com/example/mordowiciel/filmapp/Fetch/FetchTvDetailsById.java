package com.example.mordowiciel.filmapp.Fetch;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mordowiciel.filmapp.BuildConfig;
import com.example.mordowiciel.filmapp.Class.TvClass;
import com.example.mordowiciel.filmapp.Class.TvSeasonClass;

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


public class FetchTvDetailsById extends AsyncTask <String, Void, TvClass> {

    private TvDetailsCallback mTvDetailsCallback;

    public FetchTvDetailsById(Context ctx){
        mTvDetailsCallback = (TvDetailsCallback) ctx;
    }

    public interface TvDetailsCallback {
        void onAsyncExecutedPopulateView(TvClass movieDetails);
    }

    @Override
    protected TvClass doInBackground(String... params) {

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String tvDetailsJsonString = null;

        try {

            /// 1) Create the URL.

            final String MOVIEDB_AUTHORITY = "api.themoviedb.org";
            final String MOVIEDB_VERSION = "3";
            final String SHOW_TYPE = "tv";
            final String TV_ID = params[0];

            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(MOVIEDB_AUTHORITY)
                    .appendPath(MOVIEDB_VERSION)
                    .appendPath(SHOW_TYPE)
                    .appendPath(TV_ID)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US");

            URL url = new URL(builtUri.build().toString());
            Log.e ("Build URL: ", url.toString());

            /// 2) Connect to the URL.

            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            /// 3) Get data from the URL input stream.
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();

            String line;
            while ( (line = reader.readLine()) != null){
                buffer.append(line);
            }

            tvDetailsJsonString = buffer.toString();
            Log.e("TV details JSON: ", tvDetailsJsonString);
        }
        catch (IOException e) {
            Log.e("IOException : ", e.getMessage());
        }

        /// 4) Get the data from the JSON string.
        TvClass tvDetails = null;
        try {

            JSONObject jsonRoot = new JSONObject(tvDetailsJsonString);

            String id = jsonRoot.getString("id");
            String title = jsonRoot.getString("name");
            String originalTitle = jsonRoot.getString("original_name");
            String overview = jsonRoot.getString("overview");
            String airdate = jsonRoot.getString("first_air_date");
            String lastAirdate = jsonRoot.getString("last_air_date");
            double voteAverage = jsonRoot.getDouble("vote_average");

            // Get the poster for the whole series.
            String posterSubPath = jsonRoot.getString("poster_path");
            String fullPosterPath = null;

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

                fullPosterPath = posterUri.build().toString();
            }
            Log.e("TV poster link: ", "" + fullPosterPath);

            if (overview.equals("null") || overview.equals(""))
                overview = "No overview found.";

            if (airdate.equals("null") || airdate.equals(""))
                airdate = "No info found.";

            // Get information about specific seasons.
            JSONArray seasonJsonArray = jsonRoot.getJSONArray("seasons");
            ArrayList<TvSeasonClass> seasonsList = new ArrayList<>();
            for(int i = 0; i < seasonJsonArray.length(); i++){

                JSONObject season = seasonJsonArray.getJSONObject(i);
                String seasonAirDate = season.getString("air_date");
                int seasonEpisodeCount = season.getInt("episode_count");
                int seasonNumber = season.getInt("season_number");
                String seasonId = String.valueOf(season.getInt("id"));

                String seasonPosterSubpath = season.getString("poster_path");
                String seasonPosterFullPath = null;

                if (seasonPosterSubpath != "null") {
                    Uri.Builder seasonPosterUri = new Uri.Builder();
                    seasonPosterUri.scheme("https")
                            .encodedAuthority("image.tmdb.org")
                            .appendPath("t")
                            .appendPath("p")
                            .appendPath("w500")
                            // Using the substring to eliminate the first sign ("/") from the poster path.
                            // It is going to be appended by Uri.Builder.
                            .appendPath(seasonPosterSubpath.substring(1));

                    seasonPosterFullPath = seasonPosterUri.build().toString();
                }

                TvSeasonClass tvSeason = new TvSeasonClass(seasonAirDate, seasonEpisodeCount, seasonId,
                        seasonPosterFullPath, seasonNumber);
                seasonsList.add(tvSeason);
            }

            tvDetails = new TvClass(id,title,originalTitle,overview, airdate, lastAirdate,
                    voteAverage, fullPosterPath, seasonsList);

        }
        catch (JSONException e){
            Log.e("JSON parsing exc: ", e.getMessage());
        }

        return tvDetails;
    }

    @Override
    protected void onPostExecute(TvClass tvDetails){
        mTvDetailsCallback.onAsyncExecutedPopulateView(tvDetails);
    }

}
