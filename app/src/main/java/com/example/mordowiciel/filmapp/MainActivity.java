package com.example.mordowiciel.filmapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an ArrayList of movies for ArrayAdapter.
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        //Create a GridView and attach adapter to it.
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        final ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.image_item, movieList);
        gridView.setAdapter(imageAdapter);

        //Create and execute ASyncTask.
        FetchMovieDbPopular fetchPopular = new FetchMovieDbPopular(imageAdapter);
        fetchPopular.execute();

        //Create a click listener for a particular movie and start a new detailed movie activity.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String movieTitle = imageAdapter.getItem(position).getMovieTitle();
                String movieOriginalTitle = imageAdapter.getItem(position).getMovieOriginalTitle();
                String movieOverview = imageAdapter.getItem(position).getMovieOverview();
                String movieReleaseDate = imageAdapter.getItem(position).getMovieReleaseDate();
                double movieVoteAverage = imageAdapter.getItem(position).getMovieVoteAverage();
                String moviePosterLink = imageAdapter.getItem(position).getMoviePosterLink();


                Bundle intentExtras = new Bundle();
                intentExtras.putString("MOVIE_TITLE", movieTitle);
                intentExtras.putString("MOVIE_ORIG_TITLE", movieOriginalTitle);
                intentExtras.putString("MOVIE_OVERVIEW", movieOverview);
                intentExtras.putString("MOVIE_DATE", movieReleaseDate);
                intentExtras.putDouble("MOVIE_VOTE", movieVoteAverage);
                intentExtras.putString("MOVIE_POSTER", moviePosterLink);

                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                intent.putExtras(intentExtras);

                startActivity(intent);
            }

        });
    }

}
