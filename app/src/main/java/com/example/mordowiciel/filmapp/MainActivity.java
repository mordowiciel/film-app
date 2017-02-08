package com.example.mordowiciel.filmapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements SortbyFragment.NoticeSortingDialogFragment {

    private FetchMovieDbPopular fetchPopular;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a toolbar for activity.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create a gridView and attach an adapter to it.
        ArrayList<Movie> movieArray = new ArrayList<>();
        GridView gridView = (GridView) findViewById(R.id.content_main);
        imageAdapter = new ImageAdapter(this, R.layout.image_item, movieArray);
        gridView.setAdapter(imageAdapter);

        //Create ASyncTask and execute it.
        fetchPopular = new FetchMovieDbPopular(imageAdapter);
        fetchPopular.execute("popularity.desc");

        //Create a click listener for a particular movie.
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


    public void showSortingDialog(){
        SortbyFragment sortingFragment = new SortbyFragment();
        sortingFragment.show(getFragmentManager(), "sorting");
    }

    // Listeners for options provided in sorting dialog.
    @Override
    public void onDialogPopularityClick(DialogFragment dialogFragment) {
        fetchPopular = new FetchMovieDbPopular(imageAdapter);
        fetchPopular.execute("popularity.desc");
    }

    public void onDialogRatingClick(DialogFragment dialogFragment) {
        fetchPopular = new FetchMovieDbPopular(imageAdapter);
        fetchPopular.execute("vote_average.desc");
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            case R.id.action_sort:
                showSortingDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
