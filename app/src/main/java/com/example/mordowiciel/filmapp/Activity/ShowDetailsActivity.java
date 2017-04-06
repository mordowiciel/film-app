package com.example.mordowiciel.filmapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mordowiciel.filmapp.Class.TvClass;
import com.example.mordowiciel.filmapp.Fetch.FetchMovieDetailsById;
import com.example.mordowiciel.filmapp.Class.MovieClass;
import com.example.mordowiciel.filmapp.Fetch.FetchTvDetailsById;
import com.example.mordowiciel.filmapp.Fragment.MovieDetailsFragment;
import com.example.mordowiciel.filmapp.Fragment.TvDetailsFragment;
import com.example.mordowiciel.filmapp.R;

public class ShowDetailsActivity extends AppCompatActivity
        implements FetchMovieDetailsById.MovieDetailsCallback,
        FetchTvDetailsById.TvDetailsCallback{

    private Bundle receivedBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent receivedIntent = getIntent();
        receivedBundle = receivedIntent.getExtras();
        String showType = receivedBundle.getString("SHOW_TYPE");

        if (savedInstanceState == null) {

            if (showType.equals("movie")) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                movieDetailsFragment.setArguments(receivedBundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_movie_details, movieDetailsFragment)
                        .commit();
            }

            if (showType.equals("tv")) {
                TvDetailsFragment tvDetailsFragment = new TvDetailsFragment();
                tvDetailsFragment.setArguments(receivedBundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_movie_details, tvDetailsFragment)
                        .commit();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String movieTitle = receivedBundle.getString("SHOW_TITLE");
        setTitle(movieTitle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_movie_details, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(createShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAsyncExecutedPopulateView(MovieClass movieDetails) {

        MovieDetailsFragment movieDetailsFragment =
                (MovieDetailsFragment) getSupportFragmentManager().
                        findFragmentById(R.id.container_movie_details);
        movieDetailsFragment.populateView(movieDetails);
    }

    @Override
    public void onAsyncExecutedPopulateView(TvClass tvDetails) {

        TvDetailsFragment tvDetailsFragment =
                (TvDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_movie_details);
        tvDetailsFragment.populateView(tvDetails);
    }



    public Intent createShareIntent() {

        // 1) Get the specific movie ID.
        String movieId = receivedBundle.getString("SHOW_ID");

        // 2) Build the URL with the current movie ID.
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.themoviedb.org")
                .appendPath("movie")
                .appendPath(movieId);

        String mUrl = builder.build().toString();
        Log.d("Build mUrl:", mUrl);

        // 3) Create the share intent.
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
        shareIntent.setType("text/plain");

        return shareIntent;
    }

}
