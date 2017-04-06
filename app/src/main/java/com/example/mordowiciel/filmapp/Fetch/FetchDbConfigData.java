package com.example.mordowiciel.filmapp.Fetch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mordowiciel.filmapp.BuildConfig;
import com.example.mordowiciel.filmapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class FetchDbConfigData extends AsyncTask<Void, Void, String> {

    private Context ctx;

    FetchDbConfigData(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params){

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;

        //contains JSON returned by the server
        String configJsonString = null;

        // 1) Built a URI containing link to the wanted JSON request and build the URL from it.
        // 2) Create the request to the server and open the connection.
        // 3) Get the input stream from a connection and read it to the string.
        // 4) Saved base URLs to the Shared Prefs.
        try {
            //construct the URL for MovieDB

            // Step 1.
            final String MOVIEDB_CONFIG_BASE_URL = "https://api.themoviedb.org/3/configuration?";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_CONFIG_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v("builtUri: ", builtUri.toString());

            // Step 2.
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Step 3.
            //InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream==null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            configJsonString = buffer.toString();
            Log.v("Config json string: ", configJsonString);

        } catch (IOException e){
            Log.e("Get config info: ", e.getMessage(), e);
            return null;
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    Log.e ("Get config info:", e.getMessage(), e);
                }
            }
        }

        return configJsonString;
    }

    @Override
    protected void onPostExecute(String result){

        if (result == null)
            return;

        try {
            String baseUrl = getBaseUrlFromJson(result);
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(ctx.getString(R.string.saved_base_url), baseUrl);
            editor.commit();
        }
        catch (JSONException e){
            Log.e("JSON parsing error:", e.getMessage());
        }


    }

    public String getBaseUrlFromJson (String configData) throws JSONException {

        String baseUrl = null;
        JSONObject configJson = new JSONObject(configData);
        JSONObject baseUrlJson = configJson.getJSONObject("images");
        baseUrl= baseUrlJson.getString("base_url");

        return baseUrl;

    }




}
