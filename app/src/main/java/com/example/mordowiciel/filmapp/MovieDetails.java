package com.example.mordowiciel.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getExtras();

        String movieTitle = receivedBundle.getString("MOVIE_TITLE");
        String movieOriginalTitle = receivedBundle.getString("MOVIE_ORIG_TITLE");
        String movieOverview = receivedBundle.getString("MOVIE_OVERVIEW");
        String movieDate = receivedBundle.getString("MOVIE_DATE");
        double movieVote = receivedBundle.getDouble("MOVIE_VOTE");
        String moviePosterLink = receivedBundle.getString("MOVIE_POSTER");

        Log.e("MOVIE TITLE:", movieTitle);
        Log.e("MOVIE DATE: ", movieDate);

        //Set a title of an Action Bar.
        setTitle(movieTitle);

        ImageView imageView = (ImageView) findViewById(R.id.movie_details_imageview);
        Picasso.with(this).load(moviePosterLink)
                .fit()
                .into(imageView);

        TextView titleView = (TextView) findViewById(R.id.movie_details_title_textview);
        titleView.setText(movieOriginalTitle);

        TextView dateView = (TextView) findViewById(R.id.movie_details_date_textview);
        dateView.setText(movieDate);

        TextView voteView = (TextView) findViewById(R.id.movie_details_vote_textview);
        voteView.setText(Double.toString(movieVote));

        TextView overviewView = (TextView) findViewById(R.id.movie_details_overview_textview);
        overviewView.setText(movieOverview);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
