package com.example.mordowiciel.filmapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private FetchMovieDbPopular fetchPopular;
    private ImageAdapter imageAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<MovieClass> movieArray = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.fragment_main_gridview);
        imageAdapter = new ImageAdapter(getActivity(), R.layout.image_item, movieArray);

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

                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtras(intentExtras);

                startActivity(intent);
            }
        });

        return rootView;
    }

    public void showPopularMovies() {
        fetchPopular = new FetchMovieDbPopular(imageAdapter);
        fetchPopular.execute("popularity.desc");
    }

    public void showMostRatedMovies() {
        fetchPopular = new FetchMovieDbPopular(imageAdapter);
        fetchPopular.execute("vote_average.desc");
    }

}

